"""Cold-user demo: embed a brand-new student without retraining and compare
their recommendations with a comparable warm student.

This exercises the central inductive claim end to end:

1. Train GraphSAGE on the protocol dataset (the new student does NOT exist).
2. Create a new first-semester student (attributes only -- strict cold start).
3. Embed them with ``embed_cold_student`` (single forward pass, no retrain).
4. Produce their top-10 group recommendations through the production scoring
   blend, and compare with a warm student of the same study path.

Usage:
    python3 -m recsys.run_cold_user_demo
"""

from __future__ import annotations

import argparse
import statistics
import time
from copy import deepcopy

from recsys.evaluation import build_temporal_onboarding_targets
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import embed_cold_student
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.models import DatasetConfig
from recsys.models import Student
from recsys.models import semester_bucket_for
from recsys.stats_tests import per_user_ranking_metrics
from recsys.synthetic_data import generate_synthetic_dataset


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--students", type=int, default=2000)
    parser.add_argument("--groups", type=int, default=100)
    parser.add_argument("--topics", type=int, default=50)
    parser.add_argument("--epochs", type=int, default=20)
    parser.add_argument("--seed", type=int, default=42)
    parser.add_argument("--study-path", default="Computer Science and Digital Communications")
    args = parser.parse_args()

    print("[1/4] Training on the protocol dataset (new student NOT included) ...")
    dataset = generate_synthetic_dataset(
        config=DatasetConfig(
            num_students=args.students,
            num_groups=args.groups,
            num_topics=args.topics,
            messages_per_day=150,
            num_days=14,
        ),
        seed=args.seed,
    )
    train_dataset, held_out, warm_ids, _ = build_temporal_onboarding_targets(
        dataset=deepcopy(dataset), cold_start_ratio=0.25, seed=args.seed
    )
    hin = build_hin(train_dataset, max_topics=args.topics)
    prep = prepare_graphsage_training_data(
        dataset=train_dataset, hin=hin, config=GraphSAGEConfig(seed=args.seed)
    )
    result = train_graphsage_embeddings(
        prep=prep, config=GraphSAGETrainConfig(epochs=args.epochs, seed=args.seed)
    )

    print("[2/4] Creating and embedding the new cold student (no retraining) ...")
    max_semester = max(s.semester for s in train_dataset.students.values())
    declared_interests = {"algorithms": 1.0, "databases": 1.0, "ai": 1.0}
    new_student = Student(
        id="new_student_demo",
        first_name="",
        last_name="",
        study_path=args.study_path,
        semester=1,
        semester_bucket=semester_bucket_for(1),
        preferred_topics=sorted(declared_interests),
        joined_group_ids=[],
    )
    started = time.perf_counter()
    embedding_strict = embed_cold_student(
        result, prep, new_student, max_semester=max_semester
    )
    embedding = embed_cold_student(
        result, prep, new_student, max_semester=max_semester,
        topic_weights=declared_interests,
    )
    elapsed_ms = (time.perf_counter() - started) * 1000.0 / 2.0
    print(
        f"    one forward pass over the graph, no retraining: "
        f"{elapsed_ms:.0f} ms per embedding (dim={len(embedding)})"
    )

    # inject the new student + embedding, then reuse the production scorer
    demo_dataset = deepcopy(train_dataset)
    demo_dataset.students[new_student.id] = new_student
    hin.student_topic_weights[new_student.id] = {
        t: w for t, w in declared_interests.items() if t in hin.selected_topics
    }
    result.embeddings[new_student.id] = embedding
    strict_student = Student(
        id="new_student_strict",
        first_name="",
        last_name="",
        study_path=new_student.study_path,
        semester=1,
        semester_bucket=semester_bucket_for(1),
        preferred_topics=[],
        joined_group_ids=[],
    )
    demo_dataset.students[strict_student.id] = strict_student
    result.embeddings[strict_student.id] = embedding_strict
    recommendations = build_graphsage_recommendations(demo_dataset, hin, result, top_k=10)

    def path_alignment(items) -> float:
        shares = []
        for item in items[:5]:
            group = demo_dataset.groups[item["groupId"]]
            members = group.member_ids
            if members:
                same = sum(
                    1
                    for member in members
                    if demo_dataset.students[member].study_path == new_student.study_path
                )
                shares.append(same / len(members))
        return statistics.mean(shares) if shares else 0.0

    print(f"\n=== NEW COLD USER ({new_student.study_path}, semester 1) ===")
    cold_items = recommendations[new_student.id]
    for rank, item in enumerate(cold_items, 1):
        b = item["breakdown"]
        print(
            f"  {rank:2d}. {item['groupName']:<38} score={item['score']:.3f} "
            f"(path={b['studyPathAffinity']:.2f} topic={b['topicSimilarity']:.2f})"
        )
    print(f"  same-study-path member share in top-5: {path_alignment(cold_items):.2f}")

    strict_items = recommendations[strict_student.id]
    strict_names = ", ".join(item["groupName"] for item in strict_items[:3])
    print(
        "  (strict variant -- attributes only, no declared interests -- top-3: "
        f"{strict_names}; same-path share {path_alignment(strict_items):.2f})"
    )

    print("[3/4] Selecting a comparable warm student ...")
    candidates = [
        sid
        for sid in warm_ids
        if train_dataset.students[sid].study_path == new_student.study_path
    ]
    warm_id = max(candidates, key=lambda sid: len(train_dataset.students[sid].joined_group_ids))
    warm_student = train_dataset.students[warm_id]
    print(
        f"    {warm_id} (semester {warm_student.semester}, "
        f"{len(warm_student.joined_group_ids)} joined groups in training)"
    )

    print(f"\n=== WARM USER ({warm_student.study_path}, semester {warm_student.semester}) ===")
    warm_items = recommendations[warm_id]
    for rank, item in enumerate(warm_items, 1):
        b = item["breakdown"]
        print(
            f"  {rank:2d}. {item['groupName']:<38} score={item['score']:.3f} "
            f"(path={b['studyPathAffinity']:.2f} topic={b['topicSimilarity']:.2f})"
        )

    print("\n[4/4] Comparison ...")
    cold_ids_set = {item["groupId"] for item in cold_items}
    warm_ids_set = {item["groupId"] for item in warm_items}
    overlap = len(cold_ids_set & warm_ids_set)
    print(f"  top-10 overlap between the two users: {overlap}/10 groups")

    ranked = {warm_id: [str(item["groupId"]) for item in warm_items]}
    metrics = per_user_ranking_metrics(ranked, held_out, k=10).get(warm_id)
    if metrics:
        target = held_out[warm_id]
        hits = [g for g in ranked[warm_id] if g in set(target)]
        print(
            f"  warm user's held-out future join: predicted at "
            f"rank {ranked[warm_id].index(hits[0]) + 1 if hits else '>10'} "
            f"(NDCG@10={metrics['ndcg']:.3f})"
        )
    print(
        "  cold user has no ground truth yet (they just arrived); segment-level "
        "cold quality is reported by the main protocol (see recsys_experiments/summary.md)."
    )


if __name__ == "__main__":
    main()
