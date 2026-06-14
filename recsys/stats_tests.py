"""Per-user metrics, hypothesis tests, and beyond-accuracy metrics for BA2.

Operationalises BA1 Sections 6.2 and 7.4: paired t-tests and Wilcoxon
signed-rank tests with Bonferroni correction for the model comparisons (H1,
H2) and the Kruskal-Wallis H-test for study-path fairness, plus catalogue
coverage and intra-list diversity.
"""

from __future__ import annotations

import math
from statistics import mean

import numpy as np
from scipy import stats

from recsys.models import SyntheticDataset


def _dcg_at_k(relevances: list[int], k: int) -> float:
    score = 0.0
    for index, relevance in enumerate(relevances[:k]):
        if relevance:
            score += relevance / math.log2(index + 2)
    return score


def per_user_ranking_metrics(
    ranked_group_ids: dict[str, list[str]],
    held_out_group_ids: dict[str, list[str]],
    user_ids: list[str] | None = None,
    k: int = 10,
) -> dict[str, dict[str, float]]:
    """Per-user Precision@k / Recall@k / NDCG@k, the unit of statistical analysis."""
    candidate_user_ids = user_ids or sorted(held_out_group_ids.keys())
    per_user: dict[str, dict[str, float]] = {}

    for user_id in candidate_user_ids:
        relevant = set(held_out_group_ids.get(user_id, []))
        if not relevant:
            continue

        ranked = ranked_group_ids.get(user_id, [])[:k]
        hits = [1 if group_id in relevant else 0 for group_id in ranked]
        hit_count = sum(hits)
        ideal = [1] * min(len(relevant), k)
        ideal_dcg = _dcg_at_k(ideal, k)

        per_user[user_id] = {
            "precision": hit_count / max(len(ranked), 1),
            "recall": hit_count / len(relevant),
            "ndcg": _dcg_at_k(hits, k) / ideal_dcg if ideal_dcg else 0.0,
        }

    return per_user


def paired_comparison(
    per_user_a: dict[str, dict[str, float]],
    per_user_b: dict[str, dict[str, float]],
    metric: str = "ndcg",
    alpha: float = 0.05,
    num_hypotheses: int = 1,
) -> dict[str, object]:
    """Paired t-test and Wilcoxon signed-rank test of model A versus model B.

    Differences are computed per user (A - B) over the users evaluated by both
    models, following BA1 Section 7.4. The Bonferroni-adjusted significance
    level alpha / m controls the family-wise error rate.
    """
    shared_users = sorted(set(per_user_a) & set(per_user_b))
    values_a = np.asarray([per_user_a[user][metric] for user in shared_users])
    values_b = np.asarray([per_user_b[user][metric] for user in shared_users])
    differences = values_a - values_b
    alpha_adjusted = alpha / max(num_hypotheses, 1)

    result: dict[str, object] = {
        "metric": metric,
        "numUsers": len(shared_users),
        "meanA": round(float(values_a.mean()), 6) if len(shared_users) else 0.0,
        "meanB": round(float(values_b.mean()), 6) if len(shared_users) else 0.0,
        "meanDifference": round(float(differences.mean()), 6) if len(shared_users) else 0.0,
        "alpha": alpha,
        "numHypotheses": num_hypotheses,
        "alphaBonferroniAdjusted": round(alpha_adjusted, 6),
    }

    if len(shared_users) < 2 or np.allclose(differences, 0.0):
        result.update({
            "tStatistic": None, "tPValue": None,
            "wilcoxonStatistic": None, "wilcoxonPValue": None,
            "significant": False,
        })
        return result

    t_statistic, t_p_value = stats.ttest_rel(values_a, values_b)
    try:
        wilcoxon_statistic, wilcoxon_p_value = stats.wilcoxon(values_a, values_b)
    except ValueError:
        wilcoxon_statistic, wilcoxon_p_value = float("nan"), float("nan")

    # Cohen's d for paired samples: mean difference over SD of differences.
    sd_difference = float(differences.std(ddof=1))
    effect_size = float(differences.mean()) / sd_difference if sd_difference else 0.0

    result.update({
        "tStatistic": round(float(t_statistic), 6),
        "tPValue": float(t_p_value),
        "wilcoxonStatistic": round(float(wilcoxon_statistic), 6),
        "wilcoxonPValue": float(wilcoxon_p_value),
        "cohensD": round(effect_size, 6),
        "significant": bool(t_p_value < alpha_adjusted),
    })
    return result


def kruskal_wallis_by_cohort(
    dataset: SyntheticDataset,
    per_user: dict[str, dict[str, float]],
    metric: str = "ndcg",
    min_cohort_size: int = 5,
    alpha: float = 0.05,
) -> dict[str, object]:
    """Kruskal-Wallis H-test of metric equality across study-path cohorts (BA1 Section 6.2)."""
    cohorts: dict[str, list[float]] = {}
    for user_id, metrics in per_user.items():
        student = dataset.students.get(user_id)
        if student is None or not student.study_path:
            continue
        cohorts.setdefault(student.study_path, []).append(metrics[metric])

    usable = {path: values for path, values in cohorts.items() if len(values) >= min_cohort_size}
    cohort_summary = {
        path: {
            "numUsers": len(values),
            "mean": round(mean(values), 6),
            "median": round(float(np.median(values)), 6),
        }
        for path, values in sorted(usable.items())
    }

    if len(usable) < 2:
        return {
            "metric": metric,
            "cohorts": cohort_summary,
            "hStatistic": None,
            "pValue": None,
            "significantDisparity": False,
        }

    try:
        h_statistic, p_value = stats.kruskal(*usable.values())
    except ValueError:
        # All values identical across cohorts: no disparity by definition.
        return {
            "metric": metric,
            "cohorts": cohort_summary,
            "hStatistic": 0.0,
            "pValue": 1.0,
            "alpha": alpha,
            "significantDisparity": False,
        }
    return {
        "metric": metric,
        "cohorts": cohort_summary,
        "hStatistic": round(float(h_statistic), 6),
        "pValue": float(p_value),
        "alpha": alpha,
        "significantDisparity": bool(p_value < alpha),
    }


def catalogue_coverage(
    ranked_group_ids: dict[str, list[str]],
    all_group_ids: list[str],
    k: int = 10,
) -> float:
    """Share of groups appearing in at least one user's top-k list (BA1 Section 7.2)."""
    recommended: set[str] = set()
    for ranked in ranked_group_ids.values():
        recommended.update(ranked[:k])
    if not all_group_ids:
        return 0.0
    return len(recommended & set(all_group_ids)) / len(all_group_ids)


def intra_list_diversity(
    ranked_group_ids: dict[str, list[str]],
    group_embeddings: dict[str, list[float]],
    k: int = 10,
) -> float:
    """Mean pairwise cosine distance between group embeddings in each top-k list."""
    distances: list[float] = []
    for ranked in ranked_group_ids.values():
        vectors = [
            np.asarray(group_embeddings[group_id], dtype=np.float64)
            for group_id in ranked[:k]
            if group_id in group_embeddings
        ]
        vectors = [vector for vector in vectors if np.linalg.norm(vector) > 0.0]
        if len(vectors) < 2:
            continue
        matrix = np.stack(vectors)
        matrix = matrix / np.linalg.norm(matrix, axis=1, keepdims=True)
        similarity = matrix @ matrix.T
        upper = similarity[np.triu_indices(len(vectors), k=1)]
        distances.append(float(np.mean(1.0 - upper)))
    return float(mean(distances)) if distances else 0.0
