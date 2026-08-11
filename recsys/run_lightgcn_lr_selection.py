"""Validation-based selection of the LightGCN optimiser settings.

Motivation: the BPR gradient is averaged over all training triplets, so the
per-parameter update is proportional to 1/|triplets|. At protocol scale
(~21k triplets) a learning rate tuned for mini-batch training leaves the ID
embeddings essentially at their random initialisation -- the training loss
stays flat and the baseline is untrained rather than merely weak. Because
the baseline carries hypotheses H1 and H2, its learning rate must be chosen
on evidence, not by convention.

This script reuses the validation protocol of ``recsys.run_epoch_selection``:
one additional student-made membership per warm student is held out *from the
training data only*, so the test split is never touched. LightGCN is then
trained over a grid of (learning rate, epochs) settings and ranked by
validation NDCG@10.

Usage:
    python3 -m recsys.run_lightgcn_lr_selection
    python3 -m recsys.run_lightgcn_lr_selection --learning-rates 1 10 50 --epochs-grid 20 100
"""

from __future__ import annotations

import argparse
from copy import deepcopy

from recsys.evaluation import build_temporal_onboarding_targets
from recsys.evaluation import evaluate_recommendations
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.hin import build_hin
from recsys.lightgcn_train import LightGCNConfig
from recsys.lightgcn_train import build_lightgcn_recommendations
from recsys.lightgcn_train import train_lightgcn_embeddings
from recsys.models import DatasetConfig
from recsys.run_epoch_selection import build_validation_split
from recsys.synthetic_data import generate_synthetic_dataset

DEFAULT_LEARNING_RATES = [0.05, 1.0, 10.0, 50.0, 200.0, 500.0]
DEFAULT_EPOCHS_GRID = [20, 100, 300]


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
    parser.add_argument("--embedding-dim", type=int, default=16)
    parser.add_argument(
        "--learning-rates", type=float, nargs="+", default=DEFAULT_LEARNING_RATES
    )
    parser.add_argument("--epochs-grid", type=int, nargs="+", default=DEFAULT_EPOCHS_GRID)
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

    best: tuple[float, int, float] | None = None
    for learning_rate in args.learning_rates:
        for epochs in args.epochs_grid:
            result = train_lightgcn_embeddings(
                prep=prep,
                config=LightGCNConfig(
                    embedding_dim=args.embedding_dim,
                    epochs=epochs,
                    learning_rate=learning_rate,
                    seed=args.seed,
                ),
            )
            ranked = {
                student_id: [str(item["groupId"]) for item in items]
                for student_id, items in build_lightgcn_recommendations(
                    val_train, result, top_k=args.top_k
                ).items()
            }
            ndcg = evaluate_recommendations(
                val_train, ranked, val_targets, k=args.top_k
            ).to_dict()["ndcgAtK"]
            initial_loss = result.losses[0] if result.losses else float("nan")
            print(
                f"lr={learning_rate:<8g} epochs={epochs:<5d} "
                f"loss {initial_loss:.4f}->{result.final_loss:.4f}  "
                f"val-NDCG@{args.top_k}={ndcg:.4f}",
                flush=True,
            )
            if best is None or ndcg > best[2]:
                best = (learning_rate, epochs, ndcg)

    assert best is not None
    print(
        f"selected: learning_rate={best[0]:g} epochs={best[1]} "
        f"(val-NDCG@{args.top_k}={best[2]:.4f})"
    )


if __name__ == "__main__":
    main()
