"""Claim-driven tests: every capability the thesis claims, exercised the way
a user (or examiner) would actually use it.

The cold-user gap (inductive inference was claimed but not implemented) was
found by *using* the system, not by unit tests of its parts. This suite turns
that lesson into permanent coverage:

  A. privacy boundaries of the Firestore adapter,
  B. hard invariants of every recommender's output,
  C. edge cases of the post-training inductive inference path,
  D. integrity of the temporal onboarding split,
  E. metric and statistics edge cases,
  F. differential-privacy mechanics,
  G. the exact scoring-blend contract.
"""

from __future__ import annotations

import json

import numpy as np
import pytest

from recsys.evaluation import build_temporal_onboarding_targets
from recsys.evaluation import evaluate_recommendations
from recsys.evaluation import robustness_ratio
from recsys.extra_baselines import Node2VecConfig
from recsys.extra_baselines import build_node2vec_recommendations
from recsys.extra_baselines import build_popularity_recommendations
from recsys.extra_baselines import build_random_recommendations
from recsys.extra_baselines import train_node2vec_embeddings
from recsys.baselines import build_detailed_recommendations
from recsys.firestore_json_adapter import dataset_from_firestore_json
from recsys.graphsage_prep import GraphSAGEConfig
from recsys.graphsage_prep import prepare_graphsage_training_data
from recsys.graphsage_train import GraphSAGETrainConfig
from recsys.graphsage_train import build_graphsage_firestore_payloads
from recsys.graphsage_train import build_graphsage_recommendations
from recsys.graphsage_train import embed_cold_student
from recsys.graphsage_train import train_graphsage_embeddings
from recsys.hin import build_hin
from recsys.lightgcn_train import LightGCNConfig
from recsys.lightgcn_train import build_lightgcn_recommendations
from recsys.lightgcn_train import train_lightgcn_embeddings
from recsys.models import DatasetConfig
from recsys.models import Student
from recsys.models import semester_bucket_for
from recsys.privacy import DPConfig
from recsys.privacy import GradientDPConfig
from recsys.stats_tests import kruskal_wallis_by_cohort
from recsys.stats_tests import paired_comparison
from recsys.stats_tests import per_user_ranking_metrics
from recsys.synthetic_data import generate_synthetic_dataset


# ---------------------------------------------------------------------------
# Shared fixtures
# ---------------------------------------------------------------------------

@pytest.fixture(scope="module")
def dataset():
    return generate_synthetic_dataset(
        config=DatasetConfig(
            num_students=60,
            num_groups=14,
            num_topics=16,
            messages_per_day=30,
            num_days=6,
        ),
        seed=11,
    )


@pytest.fixture(scope="module")
def trained(dataset):
    hin = build_hin(dataset, max_topics=16)
    prep = prepare_graphsage_training_data(dataset=dataset, hin=hin, config=GraphSAGEConfig(seed=11))
    result = train_graphsage_embeddings(prep=prep, config=GraphSAGETrainConfig(epochs=8, seed=11))
    return hin, prep, result


def _firestore_fixture(tmp_path, rooms_extra=None, users_extra=None, messages_extra=None):
    users = [
        {"email": "Alice@stud.hcw.ac.at", "studyPath": "Computer Science", "semester": 3},
        {"email": "bob@stud.hcw.ac.at", "studyPath": "Computer Science", "semester": 3},
        {"email": "carol@stud.hcw.ac.at", "studyPath": "Nursing", "semester": 1},
    ] + (users_extra or [])
    rooms = [
        {
            "id": "room_public",
            "name": "Algorithms",
            "category": "CS",
            "description": "algorithms and data structures",
            "members": ["alice@stud.hcw.ac.at", "bob@stud.hcw.ac.at"],
            "isPrivate": False,
            "isDirect": False,
        },
    ] + (rooms_extra or [])
    messages = [
        {"id": "m1", "roomId": "room_public", "senderId": "alice@stud.hcw.ac.at", "text": "let us revise algorithms", "day": 1},
    ] + (messages_extra or [])

    paths = {}
    for name, payload in [("users", users), ("rooms", rooms), ("messages", messages)]:
        path = tmp_path / f"{name}.json"
        path.write_text(json.dumps(payload), encoding="utf-8")
        paths[name] = path
    return paths


# ---------------------------------------------------------------------------
# H. Graph integrity, degree bound, and persisted-artifact privacy
# ---------------------------------------------------------------------------

def test_hin_edges_only_reference_existing_nodes(dataset):
    hin = build_hin(dataset, max_topics=16)
    node_ids = set(hin.nodes)
    for edge in hin.edges:
        assert edge.source in node_ids, f"dangling edge source {edge.source}"
        assert edge.target in node_ids, f"dangling edge target {edge.target}"


def test_neighbour_capping_enforces_degree_bound(dataset):
    hin = build_hin(dataset, max_topics=16)
    cap = 4
    prep = prepare_graphsage_training_data(
        dataset=dataset, hin=hin, config=GraphSAGEConfig(seed=11, max_neighbors=cap)
    )
    for neighbours in prep.adjacency.values():
        assert len(neighbours) <= cap
    for neighbours_by_node in prep.relation_adjacency.values():
        for neighbours in neighbours_by_node.values():
            assert len(neighbours) <= cap


def test_persisted_snapshot_contains_no_raw_message_text(tmp_path):
    paths = _firestore_fixture(
        tmp_path,
        messages_extra=[
            {"id": "m2", "roomId": "room_public", "senderId": "bob@stud.hcw.ac.at",
             "text": "SENSITIVE personal secret token", "day": 2},
        ],
    )
    ds = dataset_from_firestore_json(paths["users"], paths["rooms"], paths["messages"])
    serialised = json.dumps(ds.to_dict()).lower()
    assert "sensitive" not in serialised
    assert "personal secret token" not in serialised
    assert ds.to_dict()["privacy"]["messageBodies"] == "redacted"
    # derived statistics survive so the snapshot is still useful
    assert all("length" in message for message in ds.to_dict()["messages"])
    # opt-out path is explicit
    assert "secret" in json.dumps(ds.to_dict(redact_message_text=False)).lower()


def test_generator_models_both_room_types_and_split_keeps_academic_as_context(dataset):
    from copy import deepcopy

    academic = [g for g in dataset.groups.values() if not g.is_student_made]
    student_made = [g for g in dataset.groups.values() if g.is_student_made]
    assert academic, "generator must produce academic scaffolding rooms"
    assert student_made, "generator must produce student-made groups"
    # every student is auto-enrolled in exactly two academic rooms:
    # a study-path room and a study-path + semester room
    for student in dataset.students.values():
        academic_joins = [
            gid for gid in student.joined_group_ids if not dataset.groups[gid].is_student_made
        ]
        assert len(academic_joins) == 2

    train, held_out, warm_ids, cold_ids = build_temporal_onboarding_targets(
        dataset=deepcopy(dataset), cold_start_ratio=0.25, seed=11
    )
    # held-out targets are exclusively student-made groups
    for targets in held_out.values():
        assert all(train.groups[gid].is_student_made for gid in targets)
    # cold users keep their academic-room membership as context (not held out)
    for student_id in cold_ids:
        academic_joins = [
            gid for gid in train.students[student_id].joined_group_ids
            if not train.groups[gid].is_student_made
        ]
        assert academic_joins, "cold student should keep academic-room context"


# ---------------------------------------------------------------------------
# A. Adapter privacy boundaries
# ---------------------------------------------------------------------------


def test_adapter_flags_academic_rooms_as_not_student_made(tmp_path):
    paths = _firestore_fixture(
        tmp_path,
        rooms_extra=[
            {"id": "room_academic", "name": "CS - Semester 3", "members": ["bob@stud.hcw.ac.at"],
             "templateRoom": True, "ownerEmail": "system", "academicRoomKind": "main"},
        ],
    )
    ds = dataset_from_firestore_json(paths["users"], paths["rooms"], paths["messages"])
    assert ds.groups["room_academic"].is_student_made is False
    assert ds.groups["room_public"].is_student_made is True

def test_private_and_direct_rooms_never_enter_the_graph(tmp_path):
    paths = _firestore_fixture(
        tmp_path,
        rooms_extra=[
            {"id": "room_private", "name": "Secret", "members": ["alice@stud.hcw.ac.at"], "isPrivate": True},
            {"id": "room_dm", "name": "DM", "members": ["alice@stud.hcw.ac.at", "bob@stud.hcw.ac.at"], "isDirect": True},
            # legacy/string field spellings must also be caught
            {"id": "room_private_legacy", "name": "Legacy", "members": ["bob@stud.hcw.ac.at"], "private": "true"},
            {"id": "room_dm_legacy", "name": "LegacyDM", "members": ["bob@stud.hcw.ac.at"], "direct": True},
        ],
        messages_extra=[
            {"id": "m2", "roomId": "room_dm", "senderId": "alice@stud.hcw.ac.at", "text": "personal conversation", "day": 1},
        ],
    )
    ds = dataset_from_firestore_json(paths["users"], paths["rooms"], paths["messages"])

    assert set(ds.groups) == {"room_public"}
    assert all(message.group_id == "room_public" for message in ds.messages)
    for student in ds.students.values():
        assert "room_private" not in student.joined_group_ids
        assert "room_dm" not in student.joined_group_ids


def test_no_raw_email_survives_pseudonymisation(tmp_path):
    paths = _firestore_fixture(tmp_path)
    ds = dataset_from_firestore_json(paths["users"], paths["rooms"], paths["messages"])
    blob = json.dumps(
        {
            "students": sorted(ds.students),
            "members": {gid: group.member_ids for gid, group in ds.groups.items()},
            "senders": [message.sender_id for message in ds.messages],
        }
    ).lower()
    for email in ["alice@stud.hcw.ac.at", "bob@stud.hcw.ac.at", "carol@stud.hcw.ac.at"]:
        assert email not in blob
    # the reverse mapping for trusted write-back exists and is complete
    assert set(ds.source_student_ids) == set(ds.students)


def test_email_case_does_not_split_identities(tmp_path):
    # Alice appears as "Alice@..." in users and "alice@..." in members/messages
    paths = _firestore_fixture(tmp_path)
    ds = dataset_from_firestore_json(paths["users"], paths["rooms"], paths["messages"])
    assert len(ds.students) == 3
    alice_ids = {sid for sid, src in ds.source_student_ids.items() if src.lower().startswith("alice")}
    assert len(alice_ids) == 1
    (alice_id,) = alice_ids
    assert "room_public" in ds.students[alice_id].joined_group_ids


def test_adapter_tolerates_missing_fields(tmp_path):
    paths = _firestore_fixture(
        tmp_path,
        users_extra=[{"email": "dave@stud.hcw.ac.at"}],  # no studyPath/semester
        rooms_extra=[{"id": "room_bare"}],  # no members/category/description
    )
    ds = dataset_from_firestore_json(paths["users"], paths["rooms"], paths["messages"])
    assert "room_bare" in ds.groups
    dave = next(s for s in ds.students.values() if ds.source_student_ids[s.id] == "dave@stud.hcw.ac.at")
    assert dave.semester == 0 and dave.study_path == ""


# ---------------------------------------------------------------------------
# B. Output invariants of every recommender
# ---------------------------------------------------------------------------

def _assert_invariants(dataset, ranked_ids: dict[str, list[str]], k: int):
    assert set(ranked_ids) == set(dataset.students)
    for student_id, group_ids in ranked_ids.items():
        joined = set(dataset.students[student_id].joined_group_ids)
        assert not joined & set(group_ids), f"joined group recommended for {student_id}"
        assert len(group_ids) <= k
        assert len(group_ids) == len(set(group_ids)), "duplicate recommendation"
        assert all(group_id in dataset.groups for group_id in group_ids)
        # Only student-made groups are recommendable; academic rooms are
        # scaffolding and must never be recommended by any model.
        assert all(
            dataset.groups[group_id].is_student_made for group_id in group_ids
        ), f"academic room recommended for {student_id}"


def test_all_models_satisfy_output_invariants(dataset, trained):
    hin, prep, result = trained
    k = 5

    graphsage = {
        sid: [str(item["groupId"]) for item in items]
        for sid, items in build_graphsage_recommendations(dataset, hin, result, top_k=k).items()
    }
    _assert_invariants(dataset, graphsage, k)

    lightgcn_result = train_lightgcn_embeddings(prep=prep, config=LightGCNConfig(epochs=8, seed=11))
    lightgcn = {
        sid: [str(item["groupId"]) for item in items]
        for sid, items in build_lightgcn_recommendations(dataset, lightgcn_result, top_k=k).items()
    }
    _assert_invariants(dataset, lightgcn, k)

    node2vec_result = train_node2vec_embeddings(dataset, Node2VecConfig(walks_per_node=2, walk_length=6, seed=11))
    _assert_invariants(dataset, build_node2vec_recommendations(dataset, node2vec_result, top_k=k), k)
    _assert_invariants(dataset, build_random_recommendations(dataset, top_k=k, seed=11), k)
    _assert_invariants(dataset, build_popularity_recommendations(dataset, top_k=k), k)


def test_graphsage_scores_are_sorted_and_payload_contract_holds(dataset, trained):
    hin, _, result = trained
    detailed = build_graphsage_recommendations(dataset, hin, result, top_k=5)
    for items in detailed.values():
        scores = [item["score"] for item in items]
        assert scores == sorted(scores, reverse=True)

    payloads = build_graphsage_firestore_payloads(dataset, hin, result, top_k=5)
    for payload in payloads.values():
        assert set(payload) >= {"recommendedRoomIds", "recommendationsUpdatedAt", "recommendationSource"}
        assert isinstance(payload["recommendedRoomIds"], list)
        assert all(isinstance(room_id, str) for room_id in payload["recommendedRoomIds"])
        assert payload["recommendationSource"] in {
            "GRAPH_SAGE_LOCAL", "LIGHT_GCN_LOCAL", "CONTENT_BASED", "POPULARITY",
        }


def test_dp_trained_model_still_satisfies_invariants(dataset, trained):
    hin, prep, _ = trained
    result = train_graphsage_embeddings(
        prep=prep,
        config=GraphSAGETrainConfig(epochs=6, dp=DPConfig(epsilon=1.0), seed=11),
    )
    ranked = {
        sid: [str(item["groupId"]) for item in items]
        for sid, items in build_graphsage_recommendations(dataset, hin, result, top_k=5).items()
    }
    _assert_invariants(dataset, ranked, 5)


# ---------------------------------------------------------------------------
# C. Inductive inference edge cases
# ---------------------------------------------------------------------------

def _new_student(**overrides):
    defaults = dict(
        id="brand-new-student",
        first_name="",
        last_name="",
        study_path="Computer Science and Digital Communications",
        semester=1,
        semester_bucket=semester_bucket_for(1),
        preferred_topics=[],
        joined_group_ids=[],
    )
    defaults.update(overrides)
    return Student(**defaults)


def test_embed_cold_student_rejects_existing_node(dataset, trained):
    _, prep, result = trained
    existing_id = next(iter(dataset.students))
    with pytest.raises(ValueError, match="already part of the training graph"):
        embed_cold_student(result, prep, _new_student(id=existing_id))


def test_embed_cold_student_requires_weights(dataset, trained):
    _, prep, result = trained
    stripped = type(result)(
        config=result.config, losses=result.losses, node_order=result.node_order,
        embeddings=result.embeddings, final_loss=result.final_loss,
        trained_at_ms=result.trained_at_ms, weights=[], feature_names=[],
    )
    with pytest.raises(ValueError, match="weights"):
        embed_cold_student(stripped, prep, _new_student())


def test_embed_cold_student_handles_unknown_attributes(trained):
    _, prep, result = trained
    # study path and topics that do not exist in the feature catalogue
    student = _new_student(study_path="Quantum Basket Weaving")
    embedding = embed_cold_student(
        result, prep, student, topic_weights={"nonexistent-topic": 1.0}
    )
    assert len(embedding) == result.config.embedding_dim
    assert np.all(np.isfinite(embedding))
    assert np.linalg.norm(embedding) > 0.0


def test_embed_cold_student_is_deterministic_and_interest_sensitive(trained):
    hin, prep, result = trained
    known_topic = hin.selected_topics[0]
    first = embed_cold_student(result, prep, _new_student(), topic_weights={known_topic: 1.0})
    second = embed_cold_student(result, prep, _new_student(), topic_weights={known_topic: 1.0})
    plain = embed_cold_student(result, prep, _new_student())
    assert first == second
    assert first != plain  # declared interests must influence the embedding


def test_new_student_gets_valid_recommendations_end_to_end(dataset, trained):
    hin, prep, result = trained
    from copy import deepcopy

    student = _new_student()
    embedding = embed_cold_student(result, prep, student)
    demo = deepcopy(dataset)
    demo.students[student.id] = student
    result.embeddings[student.id] = embedding
    try:
        items = build_graphsage_recommendations(demo, hin, result, top_k=5)[student.id]
    finally:
        del result.embeddings[student.id]
    assert 0 < len(items) <= 5
    assert all(item["groupId"] in dataset.groups for item in items)


# ---------------------------------------------------------------------------
# D. Temporal-split integrity
# ---------------------------------------------------------------------------

def test_split_removes_all_held_out_evidence(dataset):
    from copy import deepcopy

    train, held_out, warm_ids, cold_ids = build_temporal_onboarding_targets(
        dataset=deepcopy(dataset), cold_start_ratio=0.25, seed=11
    )
    cold_set = set(cold_ids)
    for student_id, group_ids in held_out.items():
        for group_id in group_ids:
            assert group_id not in train.students[student_id].joined_group_ids
            assert student_id not in train.groups[group_id].member_ids
    for message in train.messages:
        assert message.sender_id not in cold_set
    for student_id in cold_ids:
        # All student-made memberships are stripped; only academic-room
        # scaffolding context remains.
        remaining = train.students[student_id].joined_group_ids
        assert all(not train.groups[gid].is_student_made for gid in remaining)


def test_split_is_deterministic_and_seed_sensitive(dataset):
    from copy import deepcopy

    _, _, _, cold_a = build_temporal_onboarding_targets(deepcopy(dataset), seed=11)
    _, _, _, cold_b = build_temporal_onboarding_targets(deepcopy(dataset), seed=11)
    _, _, _, cold_c = build_temporal_onboarding_targets(deepcopy(dataset), seed=12)
    assert cold_a == cold_b
    assert cold_a != cold_c


# ---------------------------------------------------------------------------
# E. Metric and statistics edge cases
# ---------------------------------------------------------------------------

def test_robustness_ratio_guards_zero_division(dataset):
    ranked = {sid: [] for sid in dataset.students}
    held = {sid: ["g-000"] for sid in list(dataset.students)[:4]}
    metrics = evaluate_recommendations(dataset, ranked, held, k=5)
    assert robustness_ratio(metrics, metrics) == 0.0


def test_paired_comparison_on_identical_inputs_is_not_significant():
    per_user = {f"u{i}": {"ndcg": 0.4, "precision": 0.1, "recall": 0.2} for i in range(12)}
    outcome = paired_comparison(per_user, per_user, metric="ndcg", num_hypotheses=2)
    assert outcome["significant"] is False
    assert outcome["meanDifference"] == 0.0


def test_kruskal_wallis_handles_single_cohort(dataset):
    # force every student onto one cohort by filtering to one study path
    one_path = next(iter(dataset.students.values())).study_path
    per_user = {
        sid: {"ndcg": 0.3, "precision": 0.1, "recall": 0.1}
        for sid, student in dataset.students.items()
        if student.study_path == one_path
    }
    outcome = kruskal_wallis_by_cohort(dataset, per_user, min_cohort_size=2)
    assert outcome["significantDisparity"] is False


def test_per_user_metrics_ignore_users_without_targets():
    ranked = {"u1": ["g1"], "u2": ["g2"]}
    held = {"u1": ["g1"]}
    per_user = per_user_ranking_metrics(ranked, held, k=5)
    assert "u1" in per_user and "u2" not in per_user


# ---------------------------------------------------------------------------
# F. Differential-privacy mechanics
# ---------------------------------------------------------------------------

def test_dp_noise_actually_changes_the_model(dataset, trained):
    _, prep, clean = trained
    noised = train_graphsage_embeddings(
        prep=prep, config=GraphSAGETrainConfig(epochs=8, dp=DPConfig(epsilon=1.0), seed=11)
    )
    gradient = train_graphsage_embeddings(
        prep=prep,
        config=GraphSAGETrainConfig(
            epochs=8,
            dp_gradient=GradientDPConfig(epsilon=1.0, clip_norm=0.05, total_steps=8),
            seed=11,
        ),
    )
    sid = next(iter(clean.embeddings))
    assert clean.embeddings[sid] != noised.embeddings[sid]
    assert clean.embeddings[sid] != gradient.embeddings[sid]


def test_dp_training_is_reproducible_with_same_seed(dataset, trained):
    _, prep, _ = trained
    config = GraphSAGETrainConfig(epochs=6, dp=DPConfig(epsilon=2.0, seed=7), seed=7)
    first = train_graphsage_embeddings(prep=prep, config=config)
    second = train_graphsage_embeddings(prep=prep, config=config)
    assert first.embeddings == second.embeddings


def test_gradient_dp_config_edge_cases():
    assert np.isfinite(GradientDPConfig(total_steps=0).sigma())
    with pytest.raises(ValueError):
        GradientDPConfig(epsilon=0.0).sigma()
    with pytest.raises(ValueError):
        DPConfig(epsilon=-1.0).base_sigma()


# ---------------------------------------------------------------------------
# H. Graph integrity and data minimisation
# ---------------------------------------------------------------------------

def test_every_hin_edge_references_existing_nodes(dataset):
    hin = build_hin(dataset, max_topics=16)
    node_ids = set(hin.nodes)
    dangling = [
        (edge.source, edge.target, edge.relation)
        for edge in hin.edges
        if edge.source not in node_ids or edge.target not in node_ids
    ]
    assert dangling == []


def test_adapter_reduces_message_text_to_tokens(tmp_path):
    paths = _firestore_fixture(
        tmp_path,
        messages_extra=[
            {"id": "m9", "roomId": "room_public", "senderId": "bob@stud.hcw.ac.at",
             "text": "Hey!! Check THIS out: my secret PIN is 12345, OK?", "day": 2},
        ],
    )
    ds = dataset_from_firestore_json(paths["users"], paths["rooms"], paths["messages"])
    message = next(m for m in ds.messages if m.id == "m9")
    # original casing, punctuation, and the raw string must not survive
    assert message.text == message.text.lower()
    assert "!" not in message.text and ":" not in message.text and "," not in message.text
    assert "Hey!!" not in message.text


def test_dp_enforces_degree_bound_on_aggregation_rows():
    from recsys.graphsage_train import _cap_neighbors

    rng = np.random.default_rng(0)
    neighbors = [f"n{i}" for i in range(50)]
    capped = _cap_neighbors(neighbors, 15, rng)
    assert len(capped) == 15
    assert set(capped) <= set(neighbors)
    # deterministic with the same generator state
    assert _cap_neighbors(neighbors, 15, np.random.default_rng(0)) == _cap_neighbors(
        neighbors, 15, np.random.default_rng(0)
    )
    # no-op below the bound
    assert _cap_neighbors(neighbors[:5], 15, rng) == neighbors[:5]


# ---------------------------------------------------------------------------
# G. Scoring-blend contract (regression net for the thesis formula)
# ---------------------------------------------------------------------------

def test_score_recomposes_from_breakdown(dataset, trained):
    hin, _, result = trained
    detailed = build_graphsage_recommendations(dataset, hin, result, top_k=5)
    checked = 0
    for items in detailed.values():
        for item in items:
            b = item["breakdown"]
            expected = 0.8 * b["relevance"] + 0.15 * b["serendipity"] + 0.05 * b["popularity"]
            # emitted values are rounded to 6 decimals, so allow that much slack
            assert abs(item["score"] - expected) < 1e-5
            checked += 1
    assert checked > 0


def test_content_based_is_the_feature_only_ablation_of_the_graphsage_blend(dataset):
    """Content-Based must be the GraphSAGE blend minus the embedding term.

    The thesis presents Content-Based as the feature-only ablation that
    isolates the learned embedding's contribution. That only holds if both
    rankers share the outer blend (0.8/0.15/0.05) and if Content-Based's
    relevance is the GraphSAGE relevance with the 0.65 embedding term dropped
    and the remaining 0.20/0.10/0.05 weights renormalised over 0.35.
    """
    hin = build_hin(dataset, max_topics=16)
    detailed = build_detailed_recommendations(dataset, hin, top_k=5)
    checked = 0
    for items in detailed.values():
        for item in items:
            b = item["breakdown"]
            expected_relevance = (
                (0.20 / 0.35) * b["topicSimilarity"]
                + (0.10 / 0.35) * b["studyPathAffinity"]
                + (0.05 / 0.35) * b["semesterProximity"]
            )
            assert abs(b["relevance"] - expected_relevance) < 1e-5
            expected_score = (
                0.8 * b["relevance"] + 0.15 * b["serendipity"] + 0.05 * b["popularity"]
            )
            assert abs(item["score"] - expected_score) < 1e-5
            checked += 1
    assert checked > 0
