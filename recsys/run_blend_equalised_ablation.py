"""Blend-equalised LightGCN ablation: is H1 an architecture effect or a blend effect?

The main protocol ranks GraphSAGE through the hybrid scoring blend (embedding
similarity plus topic, study-path, and semester terms) but ranks LightGCN by
its native inner product. H1 as measured there is therefore a *system*
comparison: a difference could come from the architecture or simply from the
blend, and the two cannot be separated. The threats-to-validity discussion
names this confound; this script closes it.

It re-runs the frozen protocol split and, in addition to the two native
rankings, scores LightGCN's embeddings through the *same* blend used for
GraphSAGE (``build_lightgcn_blended_recommendations``, which calls the
GraphSAGE ranking function directly, so the blend cannot drift between
conditions). Three comparisons are reported per seed:

    GraphSAGE          vs LightGCN (native)   -- H1 as preregistered
    GraphSAGE          vs LightGCN (blended)  -- architecture, blend held equal
    LightGCN (blended) vs LightGCN (native)   -- what the blend alone is worth

Only the second isolates the inductive-versus-transductive question, and the
third measures how much of the preregistered gap the blend was carrying.

Usage:
    python3 -m recsys.run_blend_equalised_ablation
    python3 -m recsys.run_blend_equalised_ablation --seeds 42 43 44
"""

from __future__ import annotations

import argparse
import json
from copy import deepcopy
from pathlib import Path

from recsys.evaluation import build_temporal_onboarding_targets
from recsys.evaluation import evaluate_recommendations
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.lightgcn_train import LightGCNConfig
from recsys.lightgcn_train import build_lightgcn_blended_recommendations
from recsys.lightgcn_train import build_lightgcn_recommendations
from recsys.lightgcn_train import train_lightgcn_embeddings
from recsys.models import DatasetConfig
from recsys.stats_tests import paired_comparison
from recsys.stats_tests import per_user_ranking_metrics
from recsys.synthetic_data import generate_synthetic_dataset

# This ablation forms its own family: three comparisons are reported per seed,
# so the Bonferroni adjustment must cover three, not the two preregistered
# hypotheses of the main protocol. Correcting over 2 here would understate the
# family-wise error rate for comparisons that were added during analysis.
NUM_HYPOTHESES = 3


def ranked_ids(detailed):
    return {
        student_id: [str(item["groupId"]) for item in ranked]
        for student_id, ranked in detailed.items()
    }


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--students", type=int, default=2000)
    parser.add_argument("--groups", type=int, default=100)
    parser.add_argument("--topics", type=int, default=50)
    parser.add_argument("--messages-per-day", type=int, default=150)
    parser.add_argument("--days", type=int, default=14)
    parser.add_argument("--seeds", type=int, nargs="+", default=[42, 43, 44])
    parser.add_argument("--top-k", type=int, default=10)
    parser.add_argument("--cold-start-ratio", type=float, default=0.25)
    parser.add_argument("--output", default="recsys_experiments/blend_equalised.json")
    args = parser.parse_args()

    report: dict[str, object] = {"seeds": {}}

    for seed in args.seeds:
        dataset = generate_synthetic_dataset(
            config=DatasetConfig(
                num_students=args.students,
                num_groups=args.groups,
                num_topics=args.topics,
                messages_per_day=args.messages_per_day,
                num_days=args.days,
            ),
            seed=seed,
        )
        train_dataset, held_out, _, cold_ids = build_temporal_onboarding_targets(
            dataset=deepcopy(dataset),
            cold_start_ratio=args.cold_start_ratio,
            seed=seed,
        )
        hin = build_hin(train_dataset, max_topics=args.topics)

        prep = prepare_graphsage_training_data(
            dataset=train_dataset, hin=hin, config=GraphSAGEConfig(seed=seed)
        )
        graphsage = train_graphsage_embeddings(
            prep=prep, config=GraphSAGETrainConfig(seed=seed)
        )
        lightgcn = train_lightgcn_embeddings(
            prep=prep, config=LightGCNConfig(seed=seed)
        )

        rankings = {
            "graphsage": ranked_ids(
                build_graphsage_recommendations(
                    train_dataset, hin, graphsage, top_k=args.top_k
                )
            ),
            "lightgcn_native": ranked_ids(
                build_lightgcn_recommendations(train_dataset, lightgcn, top_k=args.top_k)
            ),
            "lightgcn_blended": ranked_ids(
                build_lightgcn_blended_recommendations(
                    train_dataset, hin, lightgcn, top_k=args.top_k
                )
            ),
        }

        # Segment exactly as the main protocol does: cold-user metrics come
        # from the full held-out map restricted to the cold cohort.
        per_user = {
            name: per_user_ranking_metrics(
                ranking, held_out, user_ids=cold_ids, k=args.top_k
            )
            for name, ranking in rankings.items()
        }
        cold_targets = {
            student_id: groups
            for student_id, groups in held_out.items()
            if student_id in set(cold_ids)
        }

        seed_report: dict[str, object] = {"coldUsers": len(cold_targets), "coldNdcg": {}}
        for name, ranking in rankings.items():
            seed_report["coldNdcg"][name] = evaluate_recommendations(
                train_dataset, ranking, cold_targets, k=args.top_k
            ).to_dict()["ndcgAtK"]

        comparisons = {
            "H1_preregistered_graphsage_vs_lightgcn_native": ("graphsage", "lightgcn_native"),
            "architecture_graphsage_vs_lightgcn_blended": ("graphsage", "lightgcn_blended"),
            "blend_value_lightgcn_blended_vs_native": ("lightgcn_blended", "lightgcn_native"),
        }
        seed_report["tests"] = {}
        for label, (a, b) in comparisons.items():
            seed_report["tests"][label] = paired_comparison(
                per_user[a],
                per_user[b],
                metric="ndcg",
                num_hypotheses=NUM_HYPOTHESES,
            )

        report["seeds"][str(seed)] = seed_report

        print(f"=== seed {seed} ({len(cold_targets)} cold users) ===")
        for name, value in seed_report["coldNdcg"].items():
            print(f"    {name:20} cold NDCG@10 = {value:.4f}")
        for label, test in seed_report["tests"].items():
            print(
                f"    {label:52} diff={test['meanDifference']:+.4f} "
                f"p={test['tPValue']:.3g} d={test['cohensD']:+.3f} "
                f"sig={test['significant']}"
            )
        print()

    output_path = Path(args.output)
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_text(json.dumps(report, indent=2))
    print(f"written to {output_path}")


if __name__ == "__main__":
    main()
