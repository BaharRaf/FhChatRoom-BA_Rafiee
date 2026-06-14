from __future__ import annotations

import numpy as np

from recsys.evaluation import build_temporal_onboarding_targets
from recsys.extra_baselines import Node2VecConfig
from recsys.extra_baselines import build_node2vec_recommendations
from recsys.extra_baselines import build_popularity_recommendations
from recsys.extra_baselines import build_random_recommendations
from recsys.extra_baselines import train_node2vec_embeddings
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.models import DatasetConfig
from recsys.privacy import DPConfig
from recsys.privacy import GradientDPConfig
from recsys.privacy import clip_and_noise_gradient
from recsys.privacy import perturb_relation_matrix
from recsys.stats_tests import catalogue_coverage
from recsys.stats_tests import intra_list_diversity
from recsys.stats_tests import kruskal_wallis_by_cohort
from recsys.stats_tests import paired_comparison
from recsys.stats_tests import per_user_ranking_metrics
from recsys.synthetic_data import generate_synthetic_dataset


def _small_dataset():
    return generate_synthetic_dataset(
        config=DatasetConfig(
            num_students=40,
            num_groups=10,
            num_topics=12,
            messages_per_day=20,
            num_days=5,
        ),
        seed=7,
    )


def test_random_and_popularity_exclude_joined_groups():
    dataset = _small_dataset()
    for ranked in [
        build_random_recommendations(dataset, top_k=5, seed=1),
        build_popularity_recommendations(dataset, top_k=5),
    ]:
        for student_id, group_ids in ranked.items():
            joined = set(dataset.students[student_id].joined_group_ids)
            assert not joined & set(group_ids)
            assert len(group_ids) <= 5


def test_popularity_ranks_by_member_count():
    dataset = _small_dataset()
    ranked = build_popularity_recommendations(dataset, top_k=10)
    sizes_by_group = {group_id: len(group.member_ids) for group_id, group in dataset.groups.items()}
    for group_ids in ranked.values():
        sizes = [sizes_by_group[group_id] for group_id in group_ids]
        assert sizes == sorted(sizes, reverse=True)


def test_node2vec_produces_embeddings_and_recommendations():
    dataset = _small_dataset()
    result = train_node2vec_embeddings(
        dataset,
        Node2VecConfig(embedding_dim=8, walks_per_node=3, walk_length=8, seed=3),
    )
    assert len(result.node_embeddings) == len(dataset.students) + len(dataset.groups)
    ranked = build_node2vec_recommendations(dataset, result, top_k=5)
    assert set(ranked) == set(dataset.students)


def test_node2vec_cold_student_falls_back_to_popularity():
    dataset = _small_dataset()
    train_dataset, _, _, cold_ids = build_temporal_onboarding_targets(dataset, seed=3)
    result = train_node2vec_embeddings(train_dataset, Node2VecConfig(walks_per_node=2, walk_length=6))
    ranked = build_node2vec_recommendations(train_dataset, result, top_k=5)
    popularity = build_popularity_recommendations(train_dataset, top_k=5)
    assert cold_ids
    for student_id in cold_ids:
        assert ranked[student_id] == popularity[student_id]


def test_dp_sigma_decreases_with_epsilon():
    low_privacy = DPConfig(epsilon=10.0)
    high_privacy = DPConfig(epsilon=1.0)
    assert high_privacy.base_sigma() > low_privacy.base_sigma()
    assert high_privacy.sigma_for_relation("MEMBER_OF") > high_privacy.sigma_for_relation("RELATED_TO")


def test_perturbed_matrix_stays_row_stochastic():
    rng = np.random.default_rng(0)
    matrix = np.eye(6) * 0.5 + 0.1
    matrix = matrix / matrix.sum(axis=1, keepdims=True)
    noised = perturb_relation_matrix(matrix, "MEMBER_OF", DPConfig(epsilon=2.0), rng)
    assert noised.shape == matrix.shape
    assert np.all(noised >= 0.0)
    # Rows are re-normalised; a row may only be all-zero if the noise clipped
    # every entry (an isolated node), in which case the sum stays 0.
    row_sums = noised.sum(axis=1)
    assert np.all((np.abs(row_sums - 1.0) < 1e-9) | (row_sums == 0.0))


def test_gradient_dp_sigma_decreases_with_epsilon_and_grows_with_steps():
    assert GradientDPConfig(epsilon=1.0).sigma() > GradientDPConfig(epsilon=10.0).sigma()
    assert (
        GradientDPConfig(total_steps=400).sigma()
        > GradientDPConfig(total_steps=100).sigma()
    )


def test_clip_and_noise_gradient_bounds_norm_before_noising():
    rng = np.random.default_rng(0)
    config = GradientDPConfig(epsilon=1000.0, delta=1e-5, clip_norm=1.0, total_steps=1)
    gradient = np.full((4, 4), 10.0)
    noised = clip_and_noise_gradient(gradient, config, rng)
    # With epsilon huge the noise is negligible, so the result is essentially
    # the clipped gradient with L2 norm equal to clip_norm.
    assert abs(float(np.linalg.norm(noised)) - 1.0) < 0.05


def test_graphsage_trains_with_gradient_dp():
    dataset = _small_dataset()
    hin = build_hin(dataset, max_topics=12)
    prep = prepare_graphsage_training_data(dataset, hin)
    result = train_graphsage_embeddings(
        prep,
        GraphSAGETrainConfig(
            epochs=10,
            dp_gradient=GradientDPConfig(epsilon=5.0, clip_norm=0.05, total_steps=10),
            seed=1,
        ),
    )
    assert all(np.isfinite(result.losses))
    assert all(np.all(np.isfinite(vector)) for vector in result.embeddings.values())


def test_graphsage_trains_with_dp_and_degrades_gracefully():
    dataset = _small_dataset()
    hin = build_hin(dataset, max_topics=12)
    prep = prepare_graphsage_training_data(dataset, hin)
    clean = train_graphsage_embeddings(prep, GraphSAGETrainConfig(epochs=10, seed=1))
    noised = train_graphsage_embeddings(
        prep, GraphSAGETrainConfig(epochs=10, dp=DPConfig(epsilon=1.0), seed=1)
    )
    assert len(noised.embeddings) == len(clean.embeddings)
    assert all(np.isfinite(noised.losses))


def test_per_user_metrics_and_paired_comparison():
    ranked_a = {"u1": ["g1", "g2"], "u2": ["g3", "g4"]}
    ranked_b = {"u1": ["g9", "g8"], "u2": ["g9", "g8"]}
    held_out = {"u1": ["g1"], "u2": ["g4"]}
    per_a = per_user_ranking_metrics(ranked_a, held_out, k=2)
    per_b = per_user_ranking_metrics(ranked_b, held_out, k=2)
    assert per_a["u1"]["ndcg"] == 1.0
    assert per_b["u1"]["ndcg"] == 0.0
    comparison = paired_comparison(per_a, per_b, metric="ndcg", num_hypotheses=2)
    assert comparison["numUsers"] == 2
    assert comparison["meanDifference"] > 0
    assert comparison["alphaBonferroniAdjusted"] == 0.025


def test_kruskal_wallis_runs_on_dataset_cohorts():
    dataset = _small_dataset()
    per_user = {
        student_id: {"ndcg": 0.5, "precision": 0.1, "recall": 0.2}
        for student_id in dataset.students
    }
    outcome = kruskal_wallis_by_cohort(dataset, per_user, min_cohort_size=2)
    assert "cohorts" in outcome
    # Identical scores in every cohort must never report a disparity.
    assert outcome["significantDisparity"] is False


def test_coverage_and_diversity_bounds():
    ranked = {"u1": ["g1", "g2"], "u2": ["g2", "g3"]}
    coverage = catalogue_coverage(ranked, ["g1", "g2", "g3", "g4"], k=2)
    assert coverage == 0.75
    embeddings = {"g1": [1.0, 0.0], "g2": [0.0, 1.0], "g3": [1.0, 0.0]}
    diversity = intra_list_diversity(ranked, embeddings, k=2)
    assert 0.0 <= diversity <= 2.0
