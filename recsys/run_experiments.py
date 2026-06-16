"""End-to-end BA2 experiment runner implementing the BA1 Chapter 7 protocol.

Runs, on one synthetic dataset and one shared train/test split:

1. Six recommenders: Random, Popularity, Content-Based, Node2Vec, LightGCN,
   GraphSAGE.
2. Offline ranking metrics (Precision@10, Recall@10, NDCG@10) for all users
   and stratified into warm-start and cold-start segments.
3. Hypothesis tests H1/H2 (paired t-test + Wilcoxon, Bonferroni-corrected)
   and the Kruskal-Wallis fairness test across study-path cohorts.
4. Catalogue coverage and intra-list diversity.
5. The privacy-utility sweep: GraphSAGE retrained for epsilon in
   {1, 3, 5, 8, 10} under three DP variants: meta-path-tiered aggregation
   perturbation, uniform aggregation perturbation, and DP-SGD-style gradient
   perturbation (BA1 Section 5.2).

Writes machine-readable results (results.json), publication figures (PNG and
PDF), and a human-readable summary (summary.md) to --output-dir.

Usage:
    python3 -m recsys.run_experiments --output-dir recsys_experiments
"""

from __future__ import annotations

import argparse
import json
import time
from copy import deepcopy
from pathlib import Path

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np

from recsys.baselines import build_detailed_recommendations
from recsys.evaluation import build_temporal_onboarding_targets
from recsys.evaluation import evaluate_recommendations
from recsys.evaluation import robustness_ratio
from recsys.extra_baselines import Node2VecConfig
from recsys.extra_baselines import build_node2vec_recommendations
from recsys.extra_baselines import build_popularity_recommendations
from recsys.extra_baselines import build_random_recommendations
from recsys.extra_baselines import train_node2vec_embeddings
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.lightgcn_train import LightGCNConfig
from recsys.lightgcn_train import build_lightgcn_recommendations
from recsys.lightgcn_train import train_lightgcn_embeddings
from recsys.models import DatasetConfig
from recsys.privacy import DPConfig
from recsys.privacy import GradientDPConfig
from recsys.stats_tests import catalogue_coverage
from recsys.stats_tests import intra_list_diversity
from recsys.stats_tests import kruskal_wallis_by_cohort
from recsys.stats_tests import paired_comparison
from recsys.stats_tests import per_user_ranking_metrics
from recsys.synthetic_data import generate_synthetic_dataset

MODEL_LABELS = {
    "random": "Random",
    "popularity": "Popularity",
    "content": "Content-Based",
    "node2vec": "Node2Vec",
    "lightgcn": "LightGCN",
    "graphsage": "GraphSAGE",
}

# BA1 Section 7 fixes {1, 3, 5, 8, 10}; 0.25 and 0.5 are diagnostic points
# below the protocol range that locate the onset of utility degradation.
EPSILON_SWEEP = [0.25, 0.5, 1.0, 3.0, 5.0, 8.0, 10.0]


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Run the complete BA2 evaluation protocol.")
    parser.add_argument("--students", type=int, default=2000)
    parser.add_argument("--groups", type=int, default=100)
    parser.add_argument("--topics", type=int, default=50)
    parser.add_argument("--messages-per-day", type=int, default=150)
    parser.add_argument("--days", type=int, default=14)
    parser.add_argument("--seed", type=int, default=42)
    # Validation-selected via recsys.run_epoch_selection: quality is flat for
    # <=20 epochs and declines beyond ~25 (overfitting of the contrastive
    # objective on sparse memberships).
    parser.add_argument("--epochs", type=int, default=20)
    parser.add_argument("--embedding-dim", type=int, default=16)
    parser.add_argument("--hidden-dim", type=int, default=32)
    parser.add_argument("--cold-start-ratio", type=float, default=0.25)
    parser.add_argument("--top-k", type=int, default=10)
    parser.add_argument("--skip-dp-sweep", action="store_true")
    parser.add_argument(
        "--dp-clip-norm",
        type=float,
        default=0.05,
        help="L2 clipping threshold C for the DP-SGD gradient-perturbation variant.",
    )
    parser.add_argument("--output-dir", default="recsys_experiments")
    return parser.parse_args()


def _segment_report(dataset, ranked, held_out, warm_ids, cold_ids, k):
    metrics_all = evaluate_recommendations(dataset, ranked, held_out, k=k)
    metrics_warm = evaluate_recommendations(dataset, ranked, held_out, user_ids=warm_ids, k=k)
    metrics_cold = evaluate_recommendations(dataset, ranked, held_out, user_ids=cold_ids, k=k)
    return {
        "all": metrics_all.to_dict(),
        "warm": metrics_warm.to_dict(),
        "cold": metrics_cold.to_dict(),
        "coldStartRobustnessNdcgAtK": robustness_ratio(metrics_cold, metrics_warm),
    }


def _group_topic_vectors(hin) -> dict[str, list[float]]:
    topic_index = {topic: idx for idx, topic in enumerate(hin.selected_topics)}
    vectors: dict[str, list[float]] = {}
    for group_id, weights in hin.group_topic_weights.items():
        vector = [0.0] * len(topic_index)
        for topic, weight in weights.items():
            if topic in topic_index:
                vector[topic_index[topic]] = weight
        vectors[group_id] = vector
    return vectors


def _train_graphsage(
    train_dataset,
    hin,
    args,
    dp: DPConfig | None = None,
    dp_gradient: GradientDPConfig | None = None,
):
    prep = prepare_graphsage_training_data(
        dataset=train_dataset,
        hin=hin,
        config=GraphSAGEConfig(seed=args.seed),
    )
    result = train_graphsage_embeddings(
        prep=prep,
        config=GraphSAGETrainConfig(
            hidden_dim=args.hidden_dim,
            embedding_dim=args.embedding_dim,
            epochs=args.epochs,
            dp=dp,
            dp_gradient=dp_gradient,
            seed=args.seed,
        ),
    )
    return prep, result


def _ranked_ids(detailed: dict[str, list[dict[str, object]]]) -> dict[str, list[str]]:
    return {
        student_id: [str(item["groupId"]) for item in ranked]
        for student_id, ranked in detailed.items()
    }


def run(args: argparse.Namespace) -> dict[str, object]:
    started = time.time()
    output_dir = Path(args.output_dir)
    figures_dir = output_dir / "figures"
    figures_dir.mkdir(parents=True, exist_ok=True)

    print(f"[1/7] Generating synthetic dataset ({args.students} students, {args.groups} groups) ...")
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

    print("[2/7] Building temporal-onboarding split ...")
    train_dataset, held_out, warm_ids, cold_ids = build_temporal_onboarding_targets(
        dataset=deepcopy(dataset),
        cold_start_ratio=args.cold_start_ratio,
        seed=args.seed,
    )
    hin = build_hin(train_dataset, max_topics=args.topics)

    print("[3/7] Training models ...")
    ranked_lists: dict[str, dict[str, list[str]]] = {}
    timings: dict[str, float] = {}

    t0 = time.time()
    ranked_lists["random"] = build_random_recommendations(train_dataset, top_k=args.top_k, seed=args.seed)
    timings["random"] = time.time() - t0

    t0 = time.time()
    ranked_lists["popularity"] = build_popularity_recommendations(train_dataset, top_k=args.top_k)
    timings["popularity"] = time.time() - t0

    t0 = time.time()
    ranked_lists["content"] = _ranked_ids(
        build_detailed_recommendations(train_dataset, hin, top_k=args.top_k)
    )
    timings["content"] = time.time() - t0
    print(f"    content-based done in {timings['content']:.1f}s")

    t0 = time.time()
    node2vec_result = train_node2vec_embeddings(
        train_dataset, Node2VecConfig(embedding_dim=args.embedding_dim, seed=args.seed)
    )
    ranked_lists["node2vec"] = build_node2vec_recommendations(
        train_dataset, node2vec_result, top_k=args.top_k
    )
    timings["node2vec"] = time.time() - t0
    print(f"    node2vec done in {timings['node2vec']:.1f}s")

    t0 = time.time()
    prep, graphsage_result = _train_graphsage(train_dataset, hin, args)
    ranked_lists["graphsage"] = _ranked_ids(
        build_graphsage_recommendations(train_dataset, hin, graphsage_result, top_k=args.top_k)
    )
    timings["graphsage"] = time.time() - t0
    print(f"    graphsage done in {timings['graphsage']:.1f}s")

    t0 = time.time()
    lightgcn_result = train_lightgcn_embeddings(
        prep=prep,
        config=LightGCNConfig(
            embedding_dim=args.embedding_dim,
            epochs=args.epochs,
            seed=args.seed,
        ),
    )
    ranked_lists["lightgcn"] = {
        student_id: [str(item["groupId"]) for item in ranked]
        for student_id, ranked in build_lightgcn_recommendations(
            train_dataset, lightgcn_result, top_k=args.top_k
        ).items()
    }
    timings["lightgcn"] = time.time() - t0
    print(f"    lightgcn done in {timings['lightgcn']:.1f}s")

    print("[4/7] Evaluating ranking metrics ...")
    model_reports = {
        model: _segment_report(train_dataset, ranked, held_out, warm_ids, cold_ids, args.top_k)
        for model, ranked in ranked_lists.items()
    }

    per_user = {
        model: {
            "all": per_user_ranking_metrics(ranked, held_out, k=args.top_k),
            "warm": per_user_ranking_metrics(ranked, held_out, user_ids=warm_ids, k=args.top_k),
            "cold": per_user_ranking_metrics(ranked, held_out, user_ids=cold_ids, k=args.top_k),
        }
        for model, ranked in ranked_lists.items()
    }

    print("[5/7] Statistical tests ...")
    num_hypotheses = 2  # H1 and H2 (BA1 Section 7.4)
    hypothesis_tests = {
        "H1_graphsage_vs_lightgcn_cold_ndcg": paired_comparison(
            per_user["graphsage"]["cold"],
            per_user["lightgcn"]["cold"],
            metric="ndcg",
            num_hypotheses=num_hypotheses,
        ),
        "H2_lightgcn_vs_graphsage_warm_recall": paired_comparison(
            per_user["lightgcn"]["warm"],
            per_user["graphsage"]["warm"],
            metric="recall",
            num_hypotheses=num_hypotheses,
        ),
        "supplementary_graphsage_vs_content_cold_ndcg": paired_comparison(
            per_user["graphsage"]["cold"],
            per_user["content"]["cold"],
            metric="ndcg",
            num_hypotheses=num_hypotheses,
        ),
    }

    fairness = {
        "graphsage_all": kruskal_wallis_by_cohort(train_dataset, per_user["graphsage"]["all"]),
        "graphsage_cold": kruskal_wallis_by_cohort(train_dataset, per_user["graphsage"]["cold"]),
        "lightgcn_all": kruskal_wallis_by_cohort(train_dataset, per_user["lightgcn"]["all"]),
    }

    group_vectors = _group_topic_vectors(hin)
    # Coverage is measured over the recommendable catalogue (student-made
    # groups); academic scaffolding rooms are never recommended.
    all_group_ids = sorted(
        group_id for group_id, group in train_dataset.groups.items() if group.is_student_made
    )
    beyond_accuracy = {
        model: {
            "coverage": round(catalogue_coverage(ranked, all_group_ids, k=args.top_k), 6),
            "diversity": round(intra_list_diversity(ranked, group_vectors, k=args.top_k), 6),
        }
        for model, ranked in ranked_lists.items()
    }

    dp_sweep: list[dict[str, object]] = []
    if not args.skip_dp_sweep:
        print("[6/7] Privacy-utility sweep (this is the slow part) ...")
        for epsilon in EPSILON_SWEEP:
            for variant in ["tiered", "uniform", "gradient"]:
                dp_config: DPConfig | None = None
                dp_gradient_config: GradientDPConfig | None = None
                if variant == "gradient":
                    dp_gradient_config = GradientDPConfig(
                        epsilon=epsilon,
                        clip_norm=args.dp_clip_norm,
                        total_steps=args.epochs,
                        seed=args.seed,
                    )
                else:
                    multipliers = (
                        {"high": 1.0, "medium": 0.6, "low": 0.3}
                        if variant == "tiered"
                        else {"high": 1.0, "medium": 1.0, "low": 1.0}
                    )
                    dp_config = DPConfig(
                        epsilon=epsilon,
                        tier_noise_multipliers=multipliers,
                        seed=args.seed,
                    )
                t0 = time.time()
                _, dp_result = _train_graphsage(
                    train_dataset, hin, args, dp=dp_config, dp_gradient=dp_gradient_config
                )
                dp_ranked = _ranked_ids(
                    build_graphsage_recommendations(train_dataset, hin, dp_result, top_k=args.top_k)
                )
                report = _segment_report(train_dataset, dp_ranked, held_out, warm_ids, cold_ids, args.top_k)
                dp_sweep.append(
                    {
                        "epsilon": epsilon,
                        "variant": variant,
                        "dpConfig": (
                            dp_gradient_config.to_dict()
                            if dp_gradient_config is not None
                            else dp_config.to_dict()
                        ),
                        "metrics": report,
                        "seconds": round(time.time() - t0, 1),
                    }
                )
                print(
                    f"    eps={epsilon} ({variant}): all-NDCG@10="
                    f"{report['all']['ndcgAtK']:.4f} cold-NDCG@10={report['cold']['ndcgAtK']:.4f}"
                    f" ({dp_sweep[-1]['seconds']}s)"
                )
    else:
        print("[6/7] Privacy-utility sweep skipped.")

    print("[7/7] Writing results and figures ...")
    results = {
        "protocol": {
            "datasetConfig": {
                "students": args.students,
                "groups": args.groups,
                "topics": args.topics,
                "messagesPerDay": args.messages_per_day,
                "days": args.days,
            },
            "split": "temporal-onboarding",
            "coldStartRatio": args.cold_start_ratio,
            "topK": args.top_k,
            "epochs": args.epochs,
            "embeddingDim": args.embedding_dim,
            "seed": args.seed,
        },
        "segments": {
            "warmStudents": len(warm_ids),
            "coldStudents": len(cold_ids),
            "heldOutUsers": len(held_out),
        },
        "hinSummary": {
            "nodeCounts": hin.summary()["nodeCounts"],
            "edgeCounts": hin.summary()["edgeCounts"],
        },
        "trainingData": {
            "positivePairs": len(prep.positive_pairs),
            "trainingTriplets": len(prep.training_triplets),
            "negativePairSources": prep.negative_pair_sources,
            "featureDimension": len(prep.feature_names),
        },
        "modelMetrics": model_reports,
        "hypothesisTests": hypothesis_tests,
        "fairness": fairness,
        "beyondAccuracy": beyond_accuracy,
        "dpSweep": dp_sweep,
        "timingsSeconds": {model: round(seconds, 1) for model, seconds in timings.items()},
        "graphsageLosses": graphsage_result.losses,
        "lightgcnLosses": lightgcn_result.losses,
        "totalSeconds": round(time.time() - started, 1),
    }

    output_dir.mkdir(parents=True, exist_ok=True)
    (output_dir / "results.json").write_text(json.dumps(results, indent=2), encoding="utf-8")

    _make_figures(results, dataset, figures_dir)
    _write_summary(results, output_dir / "summary.md")

    print(f"Done in {results['totalSeconds']}s. Results in {output_dir}/")
    return results


# ---------------------------------------------------------------------------
# Figures
# ---------------------------------------------------------------------------

def _save(figure, figures_dir: Path, name: str) -> None:
    for suffix in ["png", "pdf"]:
        figure.savefig(figures_dir / f"{name}.{suffix}", dpi=200, bbox_inches="tight")
    plt.close(figure)


def _make_figures(results: dict, dataset, figures_dir: Path) -> None:
    models = list(MODEL_LABELS)
    labels = [MODEL_LABELS[model] for model in models]
    reports = results["modelMetrics"]

    # Figure: NDCG@10 by model, warm vs cold.
    figure, axis = plt.subplots(figsize=(8, 4.2))
    x = np.arange(len(models))
    width = 0.38
    warm = [reports[model]["warm"]["ndcgAtK"] for model in models]
    cold = [reports[model]["cold"]["ndcgAtK"] for model in models]
    axis.bar(x - width / 2, warm, width, label="Warm-start users", color="#4878CF")
    axis.bar(x + width / 2, cold, width, label="Cold-start users", color="#EE854A")
    axis.set_xticks(x, labels, rotation=15)
    axis.set_ylabel("NDCG@10")
    axis.set_title("Ranking quality by model and user segment")
    axis.legend()
    axis.grid(axis="y", alpha=0.3)
    _save(figure, figures_dir, "fig_ndcg_warm_cold")

    # Figure: cold-start robustness ratio.
    figure, axis = plt.subplots(figsize=(7, 3.8))
    ratios = [reports[model]["coldStartRobustnessNdcgAtK"] for model in models]
    colors = ["#66A61E" if model == "graphsage" else "#888888" for model in models]
    axis.bar(labels, ratios, color=colors)
    axis.axhline(1.0, color="black", linestyle="--", linewidth=0.8)
    axis.set_ylabel("Cold-start robustness\n(NDCG cold / NDCG warm)")
    axis.set_title("Cold-start robustness by model")
    axis.tick_params(axis="x", rotation=15)
    axis.grid(axis="y", alpha=0.3)
    _save(figure, figures_dir, "fig_coldstart_robustness")

    # Figure: precision/recall on cold users.
    figure, axis = plt.subplots(figsize=(8, 4.2))
    precision = [reports[model]["cold"]["precisionAtK"] for model in models]
    recall = [reports[model]["cold"]["recallAtK"] for model in models]
    axis.bar(x - width / 2, precision, width, label="Precision@10", color="#6ACC65")
    axis.bar(x + width / 2, recall, width, label="Recall@10", color="#D65F5F")
    axis.set_xticks(x, labels, rotation=15)
    axis.set_ylabel("Score")
    axis.set_title("Precision@10 and Recall@10 on cold-start users")
    axis.legend()
    axis.grid(axis="y", alpha=0.3)
    _save(figure, figures_dir, "fig_precision_recall_cold")

    # Figure: privacy-utility trade-off.
    sweep = results.get("dpSweep", [])
    if sweep:
        figure, axis = plt.subplots(figsize=(7, 4.2))
        baseline_ndcg = reports["graphsage"]["all"]["ndcgAtK"]
        variant_labels = {
            "tiered": "Aggregation perturbation (tiered)",
            "uniform": "Aggregation perturbation (uniform)",
            "gradient": "Gradient perturbation (DP-SGD)",
        }
        for variant, style, color in [
            ("tiered", "o-", "#4878CF"),
            ("uniform", "s--", "#D65F5F"),
            ("gradient", "^-.", "#EE854A"),
        ]:
            entries = sorted(
                (entry for entry in sweep if entry["variant"] == variant),
                key=lambda entry: entry["epsilon"],
            )
            if not entries:
                continue
            axis.plot(
                [entry["epsilon"] for entry in entries],
                [entry["metrics"]["all"]["ndcgAtK"] for entry in entries],
                style,
                color=color,
                label=variant_labels.get(variant, variant),
            )
        axis.axhline(
            baseline_ndcg, color="#66A61E", linestyle=":",
            label="GraphSAGE without DP",
        )
        axis.set_xlabel("Privacy budget $\\varepsilon$")
        axis.set_ylabel("NDCG@10 (all users)")
        axis.set_title("Privacy-utility trade-off")
        axis.legend()
        axis.grid(alpha=0.3)
        _save(figure, figures_dir, "fig_privacy_utility")

    # Figure: fairness across study paths (GraphSAGE).
    cohorts = results["fairness"]["graphsage_all"]["cohorts"]
    if cohorts:
        figure, axis = plt.subplots(figsize=(8, 4.0))
        names = [path.replace(" and ", "\nand ") for path in cohorts]
        means = [cohorts[path]["mean"] for path in cohorts]
        axis.bar(names, means, color="#4878CF")
        axis.set_ylabel("Mean NDCG@10")
        axis.set_title("GraphSAGE recommendation quality per study-path cohort")
        axis.tick_params(axis="x", labelsize=8)
        axis.grid(axis="y", alpha=0.3)
        _save(figure, figures_dir, "fig_fairness_cohorts")

    # Figure: training loss curves.
    figure, axis = plt.subplots(figsize=(7, 4.0))
    axis.plot(results["graphsageLosses"], label="GraphSAGE (contrastive ranking loss)", color="#4878CF")
    axis.plot(results["lightgcnLosses"], label="LightGCN (BPR loss)", color="#D65F5F")
    axis.set_xlabel("Epoch")
    axis.set_ylabel("Training loss")
    axis.set_title("Training convergence")
    axis.legend()
    axis.grid(alpha=0.3)
    _save(figure, figures_dir, "fig_training_loss")

    # Figure: dataset group-size distribution (power-law check).
    sizes = sorted((len(group.member_ids) for group in dataset.groups.values()), reverse=True)
    figure, axis = plt.subplots(figsize=(7, 3.8))
    axis.bar(range(1, len(sizes) + 1), sizes, color="#888888", width=1.0)
    axis.set_xlabel("Group rank by size")
    axis.set_ylabel("Members")
    axis.set_title("Group size distribution in the synthetic dataset")
    axis.grid(axis="y", alpha=0.3)
    _save(figure, figures_dir, "fig_group_size_distribution")

    # Figure: coverage and diversity.
    beyond = results["beyondAccuracy"]
    figure, axis = plt.subplots(figsize=(8, 4.0))
    coverage = [beyond[model]["coverage"] for model in models]
    diversity = [beyond[model]["diversity"] for model in models]
    axis.bar(x - width / 2, coverage, width, label="Catalogue coverage", color="#956CB4")
    axis.bar(x + width / 2, diversity, width, label="Intra-list diversity", color="#8C613C")
    axis.set_xticks(x, labels, rotation=15)
    axis.set_ylabel("Score")
    axis.set_title("Beyond-accuracy metrics (top-10 lists)")
    axis.legend()
    axis.grid(axis="y", alpha=0.3)
    _save(figure, figures_dir, "fig_coverage_diversity")


def _write_summary(results: dict, path: Path) -> None:
    lines = ["# BA2 Experiment Summary", ""]
    lines.append(f"Total runtime: {results['totalSeconds']}s")
    lines.append("")
    lines.append("## Segments")
    lines.append(f"- Warm students: {results['segments']['warmStudents']}")
    lines.append(f"- Cold students: {results['segments']['coldStudents']}")
    lines.append("")
    lines.append("## Model metrics (NDCG@10 / Precision@10 / Recall@10)")
    lines.append("")
    lines.append("| Model | All | Warm | Cold | Robustness |")
    lines.append("|---|---|---|---|---|")
    for model, label in MODEL_LABELS.items():
        report = results["modelMetrics"][model]
        def cell(segment):
            metrics = report[segment]
            return f"{metrics['ndcgAtK']:.4f} / {metrics['precisionAtK']:.4f} / {metrics['recallAtK']:.4f}"
        lines.append(
            f"| {label} | {cell('all')} | {cell('warm')} | {cell('cold')} | "
            f"{report['coldStartRobustnessNdcgAtK']:.3f} |"
        )
    lines.append("")
    lines.append("## Hypothesis tests")
    for name, test in results["hypothesisTests"].items():
        lines.append(
            f"- **{name}**: mean diff = {test['meanDifference']}, "
            f"t p-value = {test.get('tPValue')}, Wilcoxon p-value = {test.get('wilcoxonPValue')}, "
            f"significant at Bonferroni-adjusted alpha: {test.get('significant')}"
        )
    lines.append("")
    lines.append("## Fairness (Kruskal-Wallis)")
    for name, test in results["fairness"].items():
        lines.append(
            f"- **{name}**: H = {test['hStatistic']}, p = {test['pValue']}, "
            f"significant disparity: {test['significantDisparity']}"
        )
    lines.append("")
    if results["dpSweep"]:
        lines.append("## Privacy-utility sweep (all-users NDCG@10)")
        lines.append("")
        lines.append("| epsilon | tiered | uniform | gradient |")
        lines.append("|---|---|---|---|")
        by_eps: dict[float, dict[str, float]] = {}
        for entry in results["dpSweep"]:
            by_eps.setdefault(entry["epsilon"], {})[entry["variant"]] = entry["metrics"]["all"]["ndcgAtK"]
        for epsilon in sorted(by_eps):
            row = by_eps[epsilon]
            lines.append(
                f"| {epsilon} | {row.get('tiered', float('nan')):.4f} "
                f"| {row.get('uniform', float('nan')):.4f} "
                f"| {row.get('gradient', float('nan')):.4f} |"
            )
    path.write_text("\n".join(lines) + "\n", encoding="utf-8")


def main() -> None:
    run(parse_args())


if __name__ == "__main__":
    main()
