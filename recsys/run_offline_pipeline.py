from __future__ import annotations

import argparse
import json
from copy import deepcopy
from pathlib import Path

from recsys.baselines import build_detailed_recommendations
from recsys.baselines import build_firestore_payloads
from recsys.evaluation import build_leave_one_out_targets
from recsys.evaluation import compare_metrics
from recsys.evaluation import evaluate_recommendations
from recsys.evaluation import robustness_ratio
from recsys.firestore_json_adapter import DEFAULT_PSEUDONYM_SALT
from recsys.firestore_json_adapter import dataset_from_firestore_json
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_firestore_payloads
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.lightgcn_train import LightGCNConfig
from recsys.lightgcn_train import build_lightgcn_firestore_payloads
from recsys.lightgcn_train import build_lightgcn_recommendations
from recsys.lightgcn_train import train_lightgcn_embeddings
from recsys.models import DatasetConfig
from recsys.models import SyntheticDataset
from recsys.synthetic_data import generate_synthetic_dataset


def _write_json(path: Path, payload: object) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(payload, indent=2), encoding="utf-8")


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Run the complete offline BA2 recommendation pipeline."
    )
    parser.add_argument("--mode", choices=["synthetic", "json"], default="synthetic")
    parser.add_argument("--students", type=int, default=120)
    parser.add_argument("--groups", type=int, default=24)
    parser.add_argument("--topics", type=int, default=30)
    parser.add_argument("--messages-per-day", type=int, default=250)
    parser.add_argument("--days", type=int, default=14)
    parser.add_argument("--seed", type=int, default=42)
    parser.add_argument("--epochs", type=int, default=120)
    parser.add_argument("--learning-rate", type=float, default=0.05)
    parser.add_argument("--graphsage-epochs", type=int)
    parser.add_argument("--graphsage-learning-rate", type=float)
    parser.add_argument("--lightgcn-epochs", type=int)
    parser.add_argument("--lightgcn-learning-rate", type=float)
    parser.add_argument("--negative-samples-per-positive", type=int, default=3)
    parser.add_argument("--hard-negative-ratio", type=float, default=0.67)
    parser.add_argument("--hidden-dim", type=int, default=32)
    parser.add_argument("--embedding-dim", type=int, default=16)
    parser.add_argument("--lightgcn-layers", type=int, default=3)
    parser.add_argument("--top-k", type=int, default=10)
    parser.add_argument("--users")
    parser.add_argument("--rooms")
    parser.add_argument("--messages")
    parser.add_argument("--pseudonym-salt", default=DEFAULT_PSEUDONYM_SALT)
    parser.add_argument(
        "--no-pseudonymize-students",
        action="store_true",
        help="Keep source student identifiers in offline JSON artifacts. Use only for local debugging.",
    )
    parser.add_argument("--output-dir", required=True)
    return parser.parse_args()


def _load_dataset(args: argparse.Namespace) -> SyntheticDataset:
    if args.mode == "json":
        if not args.users or not args.rooms or not args.messages:
            raise SystemExit("--users, --rooms, and --messages are required in json mode")
        return dataset_from_firestore_json(
            users_path=args.users,
            rooms_path=args.rooms,
            messages_path=args.messages,
            seed=args.seed,
            pseudonymize_students=not args.no_pseudonymize_students,
            pseudonym_salt=args.pseudonym_salt,
        )

    config = DatasetConfig(
        num_students=args.students,
        num_groups=args.groups,
        num_topics=args.topics,
        messages_per_day=args.messages_per_day,
        num_days=args.days,
    )
    return generate_synthetic_dataset(config=config, seed=args.seed)


def _graphsage_config(args: argparse.Namespace) -> GraphSAGEConfig:
    return GraphSAGEConfig(
        seed=args.seed,
        negative_samples_per_positive=args.negative_samples_per_positive,
        hard_negative_ratio=args.hard_negative_ratio,
    )


def _train_graphsage(args: argparse.Namespace, dataset: SyntheticDataset, hin):
    prep = prepare_graphsage_training_data(
        dataset=dataset,
        hin=hin,
        config=_graphsage_config(args),
    )
    result = train_graphsage_embeddings(
        prep=prep,
        config=GraphSAGETrainConfig(
            hidden_dim=args.hidden_dim,
            embedding_dim=args.embedding_dim,
            learning_rate=args.graphsage_learning_rate or args.learning_rate,
            epochs=args.graphsage_epochs or args.epochs,
            seed=args.seed,
        ),
    )
    return prep, result


def _train_lightgcn(args: argparse.Namespace, prep):
    return train_lightgcn_embeddings(
        prep=prep,
        config=LightGCNConfig(
            embedding_dim=args.embedding_dim,
            num_layers=args.lightgcn_layers,
            learning_rate=args.lightgcn_learning_rate or args.learning_rate,
            epochs=args.lightgcn_epochs or args.epochs,
            seed=args.seed,
        ),
    )


def _evaluation_report(args: argparse.Namespace, dataset: SyntheticDataset) -> dict[str, object]:
    train_dataset, held_out = build_leave_one_out_targets(deepcopy(dataset))
    hin = build_hin(train_dataset, max_topics=args.topics)

    baseline_ranked = {
        student_id: [item["groupId"] for item in ranked]
        for student_id, ranked in build_detailed_recommendations(
            train_dataset,
            hin,
            top_k=args.top_k,
        ).items()
    }

    prep, graphsage_result = _train_graphsage(args, train_dataset, hin)
    graphsage_ranked = {
        student_id: [item["groupId"] for item in ranked]
        for student_id, ranked in build_graphsage_recommendations(
            train_dataset,
            hin,
            graphsage_result,
            top_k=args.top_k,
        ).items()
    }

    lightgcn_result = _train_lightgcn(args, prep)
    lightgcn_ranked = {
        student_id: [item["groupId"] for item in ranked]
        for student_id, ranked in build_lightgcn_recommendations(
            train_dataset,
            lightgcn_result,
            top_k=args.top_k,
        ).items()
    }

    baseline_all = evaluate_recommendations(train_dataset, baseline_ranked, held_out, k=args.top_k)
    baseline_warm = evaluate_recommendations(
        train_dataset,
        baseline_ranked,
        held_out,
        user_ids=prep.warm_student_ids,
        k=args.top_k,
    )
    baseline_cold = evaluate_recommendations(
        train_dataset,
        baseline_ranked,
        held_out,
        user_ids=prep.cold_student_ids,
        k=args.top_k,
    )
    graphsage_all = evaluate_recommendations(train_dataset, graphsage_ranked, held_out, k=args.top_k)
    graphsage_warm = evaluate_recommendations(
        train_dataset,
        graphsage_ranked,
        held_out,
        user_ids=prep.warm_student_ids,
        k=args.top_k,
    )
    graphsage_cold = evaluate_recommendations(
        train_dataset,
        graphsage_ranked,
        held_out,
        user_ids=prep.cold_student_ids,
        k=args.top_k,
    )
    lightgcn_all = evaluate_recommendations(train_dataset, lightgcn_ranked, held_out, k=args.top_k)
    lightgcn_warm = evaluate_recommendations(
        train_dataset,
        lightgcn_ranked,
        held_out,
        user_ids=prep.warm_student_ids,
        k=args.top_k,
    )
    lightgcn_cold = evaluate_recommendations(
        train_dataset,
        lightgcn_ranked,
        held_out,
        user_ids=prep.cold_student_ids,
        k=args.top_k,
    )

    return {
        "evaluationProtocol": "leave_one_out_membership_holdout",
        "baseline": {
            "all": baseline_all.to_dict(),
            "warm": baseline_warm.to_dict(),
            "cold": baseline_cold.to_dict(),
            "coldStartRobustnessNdcgAtK": robustness_ratio(baseline_cold, baseline_warm),
        },
        "graphsageLocal": {
            "all": graphsage_all.to_dict(),
            "warm": graphsage_warm.to_dict(),
            "cold": graphsage_cold.to_dict(),
            "coldStartRobustnessNdcgAtK": robustness_ratio(graphsage_cold, graphsage_warm),
        },
        "lightgcnLocal": {
            "all": lightgcn_all.to_dict(),
            "warm": lightgcn_warm.to_dict(),
            "cold": lightgcn_cold.to_dict(),
            "coldStartRobustnessNdcgAtK": robustness_ratio(lightgcn_cold, lightgcn_warm),
        },
        "graphsageDeltaVsBaseline": {
            "all": compare_metrics(baseline_all, graphsage_all),
            "warm": compare_metrics(baseline_warm, graphsage_warm),
            "cold": compare_metrics(baseline_cold, graphsage_cold),
        },
        "lightgcnDeltaVsBaseline": {
            "all": compare_metrics(baseline_all, lightgcn_all),
            "warm": compare_metrics(baseline_warm, lightgcn_warm),
            "cold": compare_metrics(baseline_cold, lightgcn_cold),
        },
        "graphsageDeltaVsLightGCN": {
            "all": compare_metrics(lightgcn_all, graphsage_all),
            "warm": compare_metrics(lightgcn_warm, graphsage_warm),
            "cold": compare_metrics(lightgcn_cold, graphsage_cold),
        },
        "segments": {
            "warmStudents": len(prep.warm_student_ids),
            "coldStudents": len(prep.cold_student_ids),
        },
        "trainingData": {
            "positivePairs": len(prep.positive_pairs),
            "negativePairs": len(prep.negative_pairs),
            "trainingTriplets": len(prep.training_triplets),
            "negativePairSources": prep.negative_pair_sources,
        },
        "modelSummaries": {
            "graphsage": graphsage_result.summary(),
            "lightgcn": lightgcn_result.summary(),
        },
        "heldOutUsers": len(held_out),
    }


def main() -> None:
    args = parse_args()
    output_dir = Path(args.output_dir)

    dataset = _load_dataset(args)
    hin = build_hin(dataset=dataset, max_topics=args.topics)

    baseline_recommendations = build_detailed_recommendations(
        dataset=dataset,
        hin=hin,
        top_k=args.top_k,
    )
    baseline_payloads = build_firestore_payloads(dataset=dataset, hin=hin, top_k=args.top_k)

    prep, graphsage_result = _train_graphsage(args, dataset, hin)
    graphsage_recommendations = build_graphsage_recommendations(
        dataset=dataset,
        hin=hin,
        training_result=graphsage_result,
        top_k=args.top_k,
    )
    graphsage_payloads = build_graphsage_firestore_payloads(
        dataset=dataset,
        hin=hin,
        training_result=graphsage_result,
        top_k=args.top_k,
    )

    lightgcn_result = _train_lightgcn(args, prep)
    lightgcn_recommendations = build_lightgcn_recommendations(
        dataset=dataset,
        training_result=lightgcn_result,
        top_k=args.top_k,
    )
    lightgcn_payloads = build_lightgcn_firestore_payloads(
        dataset=dataset,
        training_result=lightgcn_result,
        top_k=args.top_k,
    )

    evaluation_report = _evaluation_report(args, dataset)
    manifest = {
        "mode": args.mode,
        "topK": args.top_k,
        "privacy": dataset.to_dict()["privacy"],
        "students": len(dataset.students),
        "groups": len(dataset.groups),
        "messages": len(dataset.messages),
        "topics": len(hin.selected_topics),
        "outputs": [
            "snapshot_dataset.json",
            "hin_summary.json",
            "baseline_recommendations.json",
            "baseline_firestore_payloads.json",
            "graphsage_prep_summary.json",
            "graphsage_feature_names.json",
            "graphsage_training_summary.json",
            "graphsage_losses.json",
            "graphsage_embeddings.json",
            "graphsage_recommendations.json",
            "graphsage_firestore_payloads.json",
            "lightgcn_training_summary.json",
            "lightgcn_losses.json",
            "lightgcn_embeddings.json",
            "lightgcn_recommendations.json",
            "lightgcn_firestore_payloads.json",
            "evaluation_report.json",
            "pipeline_manifest.json",
        ],
    }

    _write_json(output_dir / "snapshot_dataset.json", dataset.to_dict())
    _write_json(output_dir / "hin_summary.json", hin.summary())
    _write_json(output_dir / "baseline_recommendations.json", baseline_recommendations)
    _write_json(output_dir / "baseline_firestore_payloads.json", baseline_payloads)
    _write_json(output_dir / "graphsage_prep_summary.json", prep.summary())
    _write_json(output_dir / "graphsage_feature_names.json", prep.feature_names)
    _write_json(output_dir / "graphsage_training_summary.json", graphsage_result.summary())
    _write_json(output_dir / "graphsage_losses.json", graphsage_result.losses)
    _write_json(output_dir / "graphsage_embeddings.json", graphsage_result.embeddings)
    _write_json(output_dir / "graphsage_recommendations.json", graphsage_recommendations)
    _write_json(output_dir / "graphsage_firestore_payloads.json", graphsage_payloads)
    _write_json(output_dir / "lightgcn_training_summary.json", lightgcn_result.summary())
    _write_json(output_dir / "lightgcn_losses.json", lightgcn_result.losses)
    _write_json(
        output_dir / "lightgcn_embeddings.json",
        {
            "students": lightgcn_result.student_embeddings,
            "groups": lightgcn_result.group_embeddings,
        },
    )
    _write_json(output_dir / "lightgcn_recommendations.json", lightgcn_recommendations)
    _write_json(output_dir / "lightgcn_firestore_payloads.json", lightgcn_payloads)
    _write_json(output_dir / "evaluation_report.json", evaluation_report)
    _write_json(output_dir / "pipeline_manifest.json", manifest)

    print(json.dumps(manifest, indent=2))


if __name__ == "__main__":
    main()
