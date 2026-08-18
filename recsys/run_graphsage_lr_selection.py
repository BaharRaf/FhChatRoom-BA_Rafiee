"""Joint validation selection of the GraphSAGE learning rate and epoch budget.

``recsys.run_epoch_selection`` swept the *training length* only, at the default
learning rate, and found validation NDCG@10 flat from 0 to ~50 epochs. A flat
curve starting at the random-initialisation value is what an untrained model
looks like, and that is exactly what it was: ``_backward`` omitted the Jacobian
of the row normalisation applied in ``_forward``, so the gradient retained a
radial component that normalisation discards. Following it grew the
pre-activations without changing embedding directions, saturating tanh until
every candidate group collapsed onto a single direction. With the gradient
corrected the curve is no longer flat and the selection has to be redone.

Plain argmax of validation NDCG is *not* a safe rule here. When the embeddings
collapse, the embedding term stops discriminating between candidates and the
blend scores whatever it scores with that term switched off -- which beats
every configuration in which the embeddings still do work. A sweep maximising
NDCG alone therefore "wins" by disabling the component it is meant to tune.

The admissibility rule is structural, not score-based: a configuration whose
mean candidate-similarity spread falls below ``--spread-floor`` has embeddings
that no longer distinguish candidates, and a degenerate embedding is not a
model of anything. Selection is the argmax over admissible configurations
only, and the spread is reported for every row so the boundary is auditable.

The validation protocol is shared with ``recsys.run_epoch_selection`` and
``recsys.run_lightgcn_lr_selection``: one additional student-made membership
per warm student is held out *from the training data only*, so the test split
is never touched.

Usage:
    python3 -m recsys.run_graphsage_lr_selection
    python3 -m recsys.run_graphsage_lr_selection --learning-rates 0.05 0.1 --epochs-grid 300 600
"""

from __future__ import annotations

import argparse
import itertools
import time
from copy import deepcopy

import numpy as np

from recsys.evaluation import build_temporal_onboarding_targets
from recsys.evaluation import evaluate_recommendations
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.models import DatasetConfig
from recsys.run_epoch_selection import build_validation_split
from recsys.scoring import cosine_similarity
from recsys.synthetic_data import generate_synthetic_dataset

DEFAULT_LEARNING_RATES = [0.05, 0.1, 0.25]
DEFAULT_EPOCHS_GRID = [300, 600, 1000]
# Below this mean spread the embedding term no longer separates candidates.
DEFAULT_SPREAD_FLOOR = 0.01
# Students sampled when estimating the spread; 300 is ample and keeps the
# sweep's cost dominated by training rather than by scoring.
SPREAD_SAMPLE_STUDENTS = 300


def candidate_similarity_spread(dataset, result, student_made_ids) -> float:
    """Mean over students of the std of embedding cosine sim across candidates.

    Near zero means every candidate group looks identical to the student, so
    the embedding term contributes a constant offset and drops out of the
    ranking entirely.
    """
    vectors = {
        group_id: np.asarray(result.embeddings[group_id], dtype=np.float64)
        for group_id in student_made_ids
        if group_id in result.embeddings
    }
    spreads: list[float] = []
    for student_id, student in itertools.islice(
        sorted(dataset.students.items()), SPREAD_SAMPLE_STUDENTS
    ):
        vector = np.asarray(result.embeddings.get(student_id, []), dtype=np.float64)
        if vector.size == 0:
            continue
        joined = set(student.joined_group_ids)
        similarities = [
            cosine_similarity(vector, embedding)
            for group_id, embedding in vectors.items()
            if group_id not in joined
        ]
        if len(similarities) > 1:
            spreads.append(float(np.std(similarities)))
    return float(np.mean(spreads)) if spreads else 0.0


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--students", type=int, default=2000)
    parser.add_argument("--groups", type=int, default=100)
    parser.add_argument("--topics", type=int, default=50)
    parser.add_argument("--messages-per-day", type=int, default=150)
    parser.add_argument("--days", type=int, default=14)
    parser.add_argument("--seed", type=int, default=42)
    parser.add_argument("--top-k", type=int, default=10)
    parser.add_argument("--cold-start-ratio", type=float, default=0.25)
    parser.add_argument(
        "--learning-rates", type=float, nargs="+", default=DEFAULT_LEARNING_RATES
    )
    parser.add_argument("--epochs-grid", type=int, nargs="+", default=DEFAULT_EPOCHS_GRID)
    parser.add_argument("--spread-floor", type=float, default=DEFAULT_SPREAD_FLOOR)
    args = parser.parse_args()

    dataset = generate_synthetic_dataset(
        config=DatasetConfig(
            num_students=args.students,
            num_groups=args.groups,
            num_topics=args.topics,
            messages_per_day=args.messages_per_day,
            num_days=args.days,
        ),
        seed=args.seed,
    )
    train_dataset, _, _, _ = build_temporal_onboarding_targets(
        dataset=deepcopy(dataset),
        cold_start_ratio=args.cold_start_ratio,
        seed=args.seed,
    )
    val_train, val_targets = build_validation_split(train_dataset)
    print(f"validation users: {len(val_targets)}", flush=True)

    hin = build_hin(val_train, max_topics=args.topics)
    prep = prepare_graphsage_training_data(
        dataset=val_train, hin=hin, config=GraphSAGEConfig(seed=args.seed)
    )
    print(f"training triplets: {len(prep.training_triplets)}", flush=True)
    student_made_ids = [
        group_id
        for group_id, group in val_train.groups.items()
        if group.is_student_made
    ]

    print(
        f"{'lr':>7} {'epochs':>7} {'loss':>8} {'spread':>9} "
        f"{'NDCG@10':>8} {'sec':>7} {'admissible':>11}"
    )
    best: tuple[float, int, float, float] | None = None
    for learning_rate in args.learning_rates:
        for epochs in args.epochs_grid:
            started = time.time()
            result = train_graphsage_embeddings(
                prep=prep,
                config=GraphSAGETrainConfig(
                    epochs=epochs, learning_rate=learning_rate, seed=args.seed
                ),
            )
            elapsed = time.time() - started

            spread = candidate_similarity_spread(val_train, result, student_made_ids)
            ranked = {
                student_id: [str(item["groupId"]) for item in items]
                for student_id, items in build_graphsage_recommendations(
                    val_train, hin, result, top_k=args.top_k
                ).items()
            }
            ndcg = evaluate_recommendations(
                val_train, ranked, val_targets, k=args.top_k
            ).to_dict()["ndcgAtK"]
            admissible = spread >= args.spread_floor

            print(
                f"{learning_rate:>7g} {epochs:>7d} {result.final_loss:>8.4f} "
                f"{spread:>9.6f} {ndcg:>8.4f} {elapsed:>7.1f} {str(admissible):>11}",
                flush=True,
            )
            if admissible and (best is None or ndcg > best[2]):
                best = (learning_rate, epochs, ndcg, spread)

    print()
    if best is None:
        print(
            "no admissible configuration: the embeddings collapse at every "
            f"setting tried (spread < {args.spread_floor})"
        )
    else:
        print(
            f"selected (admissible argmax): learning_rate={best[0]:g} "
            f"epochs={best[1]} val-NDCG@{args.top_k}={best[2]:.4f} "
            f"spread={best[3]:.4f}"
        )


if __name__ == "__main__":
    main()
