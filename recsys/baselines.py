from __future__ import annotations

import time

from recsys.models import HINGraph
from recsys.models import Recommendation
from recsys.models import RecommendationBreakdown
from recsys.models import SyntheticDataset
from recsys.scoring import jaccard_similarity
from recsys.scoring import max_group_size
from recsys.scoring import peer_set
from recsys.scoring import popularity
from recsys.scoring import semester_proximity
from recsys.scoring import sparse_cosine_similarity
from recsys.scoring import study_path_affinity

# Scoring signals live in recsys.scoring (shared with the GraphSAGE ranker);
# the Content-Based ranker scores empty groups 0.0 for study-path affinity.
#
# The Content-Based baseline is the *feature-only ablation* of the GraphSAGE
# ranking blend (see graphsage_train.build_graphsage_recommendations): it is
# the same blend with the embedding-similarity term removed and the remaining
# relevance weights renormalised, so a GraphSAGE-vs-Content-Based comparison
# isolates exactly the contribution of the learned embedding.
#
#   GraphSAGE relevance : 0.65*embedding + 0.20*topic + 0.10*path + 0.05*semester
#   Content-Based       :                  0.20/0.35*topic + 0.10/0.35*path
#                                          + 0.05/0.35*semester
#   both scores         : 0.8*relevance + 0.15*serendipity + 0.05*popularity
_RELEVANCE_TOPIC_WEIGHT = 0.20 / 0.35
_RELEVANCE_PATH_WEIGHT = 0.10 / 0.35
_RELEVANCE_SEMESTER_WEIGHT = 0.05 / 0.35
_SERENDIPITY_WEIGHT = 0.15
_POPULARITY_WEIGHT = 0.05


def recommend_groups_for_student(
    dataset: SyntheticDataset,
    hin: HINGraph,
    student_id: str,
    top_k: int = 10,
    lambda_relevance: float = 0.8,
) -> list[Recommendation]:
    student = dataset.students[student_id]
    joined_group_ids = set(student.joined_group_ids)
    peer_ids = peer_set(dataset, student_id)
    largest_group = max_group_size(dataset)

    recommendations: list[Recommendation] = []

    for group_id, group in dataset.groups.items():
        if group_id in joined_group_ids or not group.is_student_made:
            continue

        topic_similarity = sparse_cosine_similarity(
            hin.student_topic_weights.get(student_id, {}),
            hin.group_topic_weights.get(group_id, {}),
        )
        path_affinity = study_path_affinity(
            dataset, student_id, group_id, empty_group_uses_primary_path=False
        )
        sem_proximity = semester_proximity(dataset, student_id, group_id)
        group_popularity = popularity(dataset, group_id, largest_group)
        member_similarity = jaccard_similarity(peer_ids, set(group.member_ids))
        serendipity = topic_similarity * (1.0 - member_similarity)
        relevance = (
            _RELEVANCE_TOPIC_WEIGHT * topic_similarity
            + _RELEVANCE_PATH_WEIGHT * path_affinity
            + _RELEVANCE_SEMESTER_WEIGHT * sem_proximity
        )
        score = (
            (lambda_relevance * relevance)
            + (_SERENDIPITY_WEIGHT * serendipity)
            + (_POPULARITY_WEIGHT * group_popularity)
        )

        recommendations.append(
            Recommendation(
                student_id=student_id,
                group_id=group_id,
                group_name=group.name,
                score=score,
                breakdown=RecommendationBreakdown(
                    topic_similarity=topic_similarity,
                    study_path_affinity=path_affinity,
                    semester_proximity=sem_proximity,
                    popularity=group_popularity,
                    serendipity=serendipity,
                    relevance=relevance,
                ),
            )
        )

    recommendations.sort(key=lambda recommendation: (-recommendation.score, recommendation.group_name))
    return recommendations[:top_k]


def build_firestore_payloads(
    dataset: SyntheticDataset,
    hin: HINGraph,
    top_k: int = 10,
    recommendation_source: str = "CONTENT_BASED",
    use_source_student_ids: bool = True,
) -> dict[str, dict[str, object]]:
    generated_at = int(time.time() * 1000)
    payloads: dict[str, dict[str, object]] = {}

    for student_id in dataset.students:
        payload_student_id = dataset.source_student_id_for(student_id) if use_source_student_ids else student_id
        recommendations = recommend_groups_for_student(
            dataset=dataset,
            hin=hin,
            student_id=student_id,
            top_k=top_k,
        )
        payloads[payload_student_id] = {
            "recommendedRoomIds": [recommendation.group_id for recommendation in recommendations],
            "recommendationsUpdatedAt": generated_at,
            "recommendationSource": recommendation_source,
        }

    return payloads


def build_detailed_recommendations(
    dataset: SyntheticDataset,
    hin: HINGraph,
    top_k: int = 10,
) -> dict[str, list[dict[str, object]]]:
    detailed: dict[str, list[dict[str, object]]] = {}

    for student_id in dataset.students:
        detailed[student_id] = [
            recommendation.to_dict()
            for recommendation in recommend_groups_for_student(
                dataset=dataset,
                hin=hin,
                student_id=student_id,
                top_k=top_k,
            )
        ]

    return detailed
