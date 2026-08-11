"""Validation-based selection of the GraphSAGE training length.

Carves a validation split out of the *training* data only (one additional
held-out membership per warm student with at least two remaining
memberships, mirroring the warm-test construction), trains GraphSAGE for a
grid of epoch counts, and reports validation NDCG@10 per setting. The test
split is never touched, so the selected value can be used in the main
protocol without test-set leakage.

Finding on the BA1 protocol dataset (seed 42): validation NDCG@10 is flat
for <= 20 epochs (~0.188, indistinguishable from random-weight aggregation)
and declines monotonically beyond ~25 epochs -- the contrastive objective
overfits the sparse membership triplets. The protocol therefore uses the
validation-selected value (20) rather than a larger budget.

Usage:
    python3 -m recsys.run_epoch_selection
"""

from __future__ import annotations

import argparse
from copy import deepcopy

from recsys.evaluation import build_temporal_onboarding_targets
from recsys.evaluation import evaluate_recommendations
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.models import DatasetConfig
from recsys.synthetic_data import generate_synthetic_dataset

DEFAULT_GRID = [0, 5, 10, 15, 20, 25, 40, 50, 75, 100, 150]


def build_validation_split(train_dataset):
    """Holds out one more *student-made* membership per warm student (train data only).

    The recommender only ranks student-made groups (academic rooms are
    auto-assigned scaffolding, never recommendation targets), so the held-out
    validation membership must itself be student-made -- otherwise it can never
    appear in a ranked list and validation NDCG is zero by construction. The
    last student-made membership is held out while the student keeps every
    academic room and any earlier student-made memberships as context, exactly
    mirroring the warm-test construction in build_temporal_onboarding_targets.
    """
    val_train = deepcopy(train_dataset)
    val_targets: dict[str, list[str]] = {}
    for student_id in sorted(val_train.students):
        student = val_train.students[student_id]
        student_made = [
            group_id
            for group_id in student.joined_group_ids
            if val_train.groups[group_id].is_student_made
        ]
        # Need a student-made membership to hold out and at least one remaining
        # membership (academic or student-made) to embed the student from.
        if student_made and len(student.joined_group_ids) >= 2:
            group_id = student_made[-1]
            student.joined_group_ids.remove(group_id)
            val_targets[student_id] = [group_id]
            if student_id in val_train.groups[group_id].member_ids:
                val_train.groups[group_id].member_ids.remove(student_id)
    return val_train, val_targets


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
    parser.add_argument("--grid", type=int, nargs="+", default=DEFAULT_GRID)
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
    print(f"validation users: {len(val_targets)}")

    hin = build_hin(val_train, max_topics=args.topics)
    prep = prepare_graphsage_training_data(
        dataset=val_train, hin=hin, config=GraphSAGEConfig(seed=args.seed)
    )

    best: tuple[int, float] | None = None
    for epochs in args.grid:
        result = train_graphsage_embeddings(
            prep=prep, config=GraphSAGETrainConfig(epochs=epochs, seed=args.seed)
        )
        ranked = {
            student_id: [str(item["groupId"]) for item in items]
            for student_id, items in build_graphsage_recommendations(
                val_train, hin, result, top_k=args.top_k
            ).items()
        }
        ndcg = evaluate_recommendations(val_train, ranked, val_targets, k=args.top_k).to_dict()[
            "ndcgAtK"
        ]
        print(f"epochs={epochs:4d}  val-NDCG@{args.top_k}={ndcg:.4f}")
        if best is None or ndcg > best[1]:
            best = (epochs, ndcg)

    assert best is not None
    print(f"selected epochs: {best[0]} (val-NDCG@{args.top_k}={best[1]:.4f})")


if __name__ == "__main__":
    main()
