from __future__ import annotations

import math
import time
from dataclasses import asdict
from dataclasses import dataclass
from dataclasses import field
from types import SimpleNamespace

import numpy as np

from recsys.graphsage_prep import GraphSAGEPreparedData
from recsys.models import HINGraph
from recsys.models import Recommendation
from recsys.models import RecommendationBreakdown
from recsys.models import SyntheticDataset
from recsys.privacy import DPConfig
from recsys.privacy import GradientDPConfig
from recsys.privacy import clip_and_noise_gradient
from recsys.privacy import perturb_relation_matrix
from recsys.scoring import cosine_similarity
from recsys.scoring import jaccard_similarity
from recsys.scoring import max_group_size
from recsys.scoring import peer_set
from recsys.scoring import popularity
from recsys.scoring import semester_proximity
from recsys.scoring import sparse_cosine_similarity
from recsys.scoring import study_path_affinity


@dataclass(frozen=True)
class GraphSAGETrainConfig:
    hidden_dim: int = 32
    embedding_dim: int = 16
    num_layers: int = 2
    learning_rate: float = 0.05
    # Validated training length (recsys.run_epoch_selection): quality is flat
    # from 0 to ~50 epochs; the protocol fixes 20 on that plateau.
    epochs: int = 20
    weight_decay: float = 1e-4
    self_loop_weight: float = 0.35
    relation_weights: dict[str, float] = field(
        default_factory=lambda: {
            "MEMBER_OF": 1.6,
            # Friendship is a deliberate, mutually confirmed tie, so it is
            # weighted just below co-membership and above topical interest.
            "FRIENDS_WITH": 1.45,
            "INTERESTED_IN": 1.35,
            "RELATED_TO": 1.35,
            "SENDS": 0.85,
            "POSTED_IN": 0.75,
            "CONTAINS": 0.75,
        }
    )
    dp: DPConfig | None = None
    dp_gradient: GradientDPConfig | None = None
    seed: int = 42


@dataclass
class GraphSAGETrainingResult:
    config: GraphSAGETrainConfig
    losses: list[float]
    node_order: list[str]
    embeddings: dict[str, list[float]]
    final_loss: float
    trained_at_ms: int
    # Trained aggregation weights and the feature catalogue: together they
    # enable inductive inference for students who were not part of the
    # training graph (see embed_cold_student).
    weights: list[np.ndarray] = field(default_factory=list)
    feature_names: list[str] = field(default_factory=list)

    def summary(self) -> dict[str, object]:
        return {
            "config": asdict(self.config),
            "epochs": len(self.losses),
            "initialLoss": self.losses[0] if self.losses else None,
            "finalLoss": self.final_loss,
            "numEmbeddings": len(self.embeddings),
            "trainedAtMs": self.trained_at_ms,
        }


def _normalize_rows(matrix: np.ndarray) -> np.ndarray:
    norms = np.linalg.norm(matrix, axis=1, keepdims=True)
    norms[norms == 0.0] = 1.0
    return matrix / norms


def _normalize_adjacency(matrix: np.ndarray) -> np.ndarray:
    degree = matrix.sum(axis=1, keepdims=True)
    degree[degree == 0.0] = 1.0
    return matrix / degree


def _cap_neighbors(
    neighbors: list[str],
    degree_bound: int,
    rng: np.random.Generator,
) -> list[str]:
    """Deterministically subsamples a neighbour list to the DP degree bound.

    The Gaussian-mechanism sensitivity in :class:`recsys.privacy.DPConfig`
    assumes every aggregation row has degree at most ``degree_bound``; this
    enforces that assumption rather than merely stating it.
    """
    if degree_bound <= 0 or len(neighbors) <= degree_bound:
        return neighbors
    chosen = rng.choice(len(neighbors), size=degree_bound, replace=False)
    return [neighbors[index] for index in sorted(chosen)]


def _build_relation_matrix(
    neighbors_by_node: dict[str, list[str]],
    node_index: dict[str, int],
    size: int,
    degree_bound: int = 0,
    rng: np.random.Generator | None = None,
) -> np.ndarray:
    matrix = np.zeros((size, size), dtype=np.float64)
    for source_id, neighbors in sorted(neighbors_by_node.items()):
        if source_id not in node_index:
            continue
        if degree_bound and rng is not None:
            neighbors = _cap_neighbors(sorted(neighbors), degree_bound, rng)
        source_index = node_index[source_id]
        for neighbor_id in neighbors:
            if neighbor_id in node_index:
                matrix[source_index, node_index[neighbor_id]] = 1.0
    return _normalize_adjacency(matrix)


def _build_matrices(
    prep: GraphSAGEPreparedData,
    config: GraphSAGETrainConfig,
) -> tuple[list[str], dict[str, int], np.ndarray, np.ndarray]:
    node_order = sorted(prep.node_features.keys())
    node_index = {node_id: index for index, node_id in enumerate(node_order)}
    features = np.asarray([prep.node_features[node_id] for node_id in node_order], dtype=np.float64)
    features = _normalize_rows(features)

    dp_rng = np.random.default_rng(config.dp.seed) if config.dp is not None else None

    adjacency = np.eye(len(node_order), dtype=np.float64) * config.self_loop_weight
    for relation, neighbors_by_node in sorted(prep.relation_adjacency.items()):
        relation_weight = config.relation_weights.get(relation, 1.0)
        if relation_weight <= 0.0:
            continue
        relation_matrix = _build_relation_matrix(
            neighbors_by_node,
            node_index,
            len(node_order),
            degree_bound=config.dp.degree_bound if config.dp is not None else 0,
            rng=dp_rng,
        )
        if config.dp is not None and dp_rng is not None:
            relation_matrix = perturb_relation_matrix(relation_matrix, relation, config.dp, dp_rng)
        adjacency += relation_weight * relation_matrix

    adjacency = _normalize_adjacency(adjacency)
    return node_order, node_index, features, adjacency


def _triplet_indices(triplets: list[tuple[str, str, str]], node_index: dict[str, int]) -> np.ndarray:
    mapped = [
        (node_index[student_id], node_index[positive_group_id], node_index[negative_group_id])
        for student_id, positive_group_id, negative_group_id in triplets
        if student_id in node_index and positive_group_id in node_index and negative_group_id in node_index
    ]
    return np.asarray(mapped, dtype=np.int64)


def _init_weight(rng: np.random.Generator, input_dim: int, output_dim: int) -> np.ndarray:
    scale = math.sqrt(2.0 / max(input_dim + output_dim, 1))
    return rng.normal(0.0, scale, size=(input_dim, output_dim))


def _forward(features: np.ndarray, adjacency: np.ndarray, weights: list[np.ndarray]) -> tuple[list[dict[str, np.ndarray]], np.ndarray]:
    current = features
    caches: list[dict[str, np.ndarray]] = []

    for weight in weights:
        previous = current
        neighbor_mean = adjacency @ previous
        layer_input = np.concatenate([previous, neighbor_mean], axis=1)
        pre_activation = layer_input @ weight
        current = np.tanh(pre_activation)
        caches.append(
            {
                "previous": previous,
                "input": layer_input,
                "pre_activation": pre_activation,
            }
        )

    embeddings = _normalize_rows(current)
    return caches, embeddings


def _ranking_loss_and_gradient(
    embeddings: np.ndarray,
    triplet_indices: np.ndarray,
) -> tuple[float, np.ndarray]:
    grad_embeddings = np.zeros_like(embeddings)
    if len(triplet_indices) == 0:
        return 0.0, grad_embeddings

    student_indices = triplet_indices[:, 0]
    positive_indices = triplet_indices[:, 1]
    negative_indices = triplet_indices[:, 2]

    student_vectors = embeddings[student_indices]
    positive_vectors = embeddings[positive_indices]
    negative_vectors = embeddings[negative_indices]

    score_difference = np.sum(student_vectors * (positive_vectors - negative_vectors), axis=1)
    score_difference = np.clip(score_difference, -20.0, 20.0)
    probabilities = 1.0 / (1.0 + np.exp(-score_difference))
    safe_probabilities = np.clip(probabilities, 1e-9, 1.0)
    loss = float(-np.mean(np.log(safe_probabilities)))

    coefficients = (probabilities - 1.0).reshape(-1, 1)
    np.add.at(grad_embeddings, student_indices, coefficients * (positive_vectors - negative_vectors))
    np.add.at(grad_embeddings, positive_indices, coefficients * student_vectors)
    np.add.at(grad_embeddings, negative_indices, -coefficients * student_vectors)

    grad_embeddings /= len(triplet_indices)
    return loss, grad_embeddings


def _backward(
    features: np.ndarray,
    adjacency: np.ndarray,
    weights: list[np.ndarray],
    caches: list[dict[str, np.ndarray]],
    grad_output: np.ndarray,
    weight_decay: float,
) -> list[np.ndarray]:
    gradients = [np.zeros_like(weight) for weight in weights]
    current_grad = grad_output

    for layer_index in reversed(range(len(weights))):
        weight = weights[layer_index]
        previous = features if layer_index == 0 else np.tanh(caches[layer_index - 1]["pre_activation"])
        pre_activation = caches[layer_index]["pre_activation"]
        layer_input = caches[layer_index]["input"]

        activation_grad = current_grad * (1.0 - np.tanh(pre_activation) ** 2)
        # The loss gradient is already a mean over training triplets; an extra
        # division by the node count would shrink updates with graph size and
        # stall training on protocol-scale graphs.
        gradients[layer_index] = layer_input.T @ activation_grad
        gradients[layer_index] += weight_decay * weight

        grad_input = activation_grad @ weight.T
        previous_dim = previous.shape[1]
        grad_self = grad_input[:, :previous_dim]
        grad_neighbors = grad_input[:, previous_dim:]
        current_grad = grad_self + adjacency.T @ grad_neighbors

    return gradients


def train_graphsage_embeddings(
    prep: GraphSAGEPreparedData,
    config: GraphSAGETrainConfig | None = None,
) -> GraphSAGETrainingResult:
    config = config or GraphSAGETrainConfig(seed=42)
    rng = np.random.default_rng(config.seed)

    node_order, node_index, features, adjacency = _build_matrices(prep, config)
    triplet_indices = _triplet_indices(prep.training_triplets, node_index)

    weights: list[np.ndarray] = []
    dimensions = [config.hidden_dim] * max(config.num_layers - 1, 0) + [config.embedding_dim]
    current_dim = features.shape[1]
    for output_dim in dimensions:
        weights.append(_init_weight(rng, current_dim * 2, output_dim))
        current_dim = output_dim

    losses: list[float] = []
    gradient_dp_rng = (
        np.random.default_rng(config.dp_gradient.seed)
        if config.dp_gradient is not None
        else None
    )

    for _ in range(config.epochs):
        caches, embeddings = _forward(features, adjacency, weights)
        loss, grad_embeddings = _ranking_loss_and_gradient(embeddings, triplet_indices)
        gradients = _backward(
            features=features,
            adjacency=adjacency,
            weights=weights,
            caches=caches,
            grad_output=grad_embeddings,
            weight_decay=config.weight_decay,
        )
        for index, gradient in enumerate(gradients):
            if config.dp_gradient is not None and gradient_dp_rng is not None:
                gradient = clip_and_noise_gradient(gradient, config.dp_gradient, gradient_dp_rng)
            weights[index] -= config.learning_rate * gradient
        losses.append(loss)

    _, final_embeddings = _forward(features, adjacency, weights)
    embedding_map = {
        node_id: final_embeddings[index].tolist()
        for index, node_id in enumerate(node_order)
    }

    return GraphSAGETrainingResult(
        config=config,
        losses=losses,
        node_order=node_order,
        embeddings=embedding_map,
        final_loss=losses[-1] if losses else 0.0,
        trained_at_ms=int(time.time() * 1000),
        weights=[weight.copy() for weight in weights],
        feature_names=list(prep.feature_names),
    )


def embed_cold_student(
    result: GraphSAGETrainingResult,
    prep: GraphSAGEPreparedData,
    student,
    max_semester: int = 6,
    topic_weights: dict[str, float] | None = None,
) -> list[float]:
    """Embeds a student who was NOT part of the training graph.

    This is the inductive path validated by the thesis: the trained weight
    matrices are shared across nodes, so a brand-new student is embedded by
    appending their node to the graph and running a single forward pass --
    no retraining. A strictly cold student (attributes only) contributes a
    self-loop-only row; if declared topical interests or first-message
    topics are known (``topic_weights``), the corresponding INTERESTED_IN
    edges to existing Topic nodes are included, exactly as the trainer
    would build them.
    """
    if not result.weights or not result.feature_names:
        raise ValueError("training result does not carry weights; retrain with this version")
    if student.id in prep.node_features:
        raise ValueError(f"student {student.id} is already part of the training graph")

    available = set(result.feature_names)
    updates: dict[str, float] = {
        "numeric:semester_norm": student.semester / max(max_semester, 1),
        "numeric:is_student": 1.0,
    }
    for one_hot in (
        f"study_path:{student.study_path}",
        f"semester_bucket:{student.semester_bucket}",
    ):
        if one_hot in available:
            updates[one_hot] = 1.0
    interest_topics: list[str] = []
    for topic, weight in (topic_weights or {}).items():
        key = f"topic:{topic}"
        if key in available:
            updates[key] = weight
        if key in prep.node_features:
            interest_topics.append(key)

    feature_vector = [updates.get(name, 0.0) for name in result.feature_names]

    # Extend the graph by the single new node (the originals stay untouched).
    extended_features = dict(prep.node_features)
    extended_features[student.id] = feature_vector
    extended_relations = {
        relation: dict(neighbors) for relation, neighbors in prep.relation_adjacency.items()
    }
    if interest_topics:
        interested = extended_relations.setdefault("INTERESTED_IN", {})
        interested[student.id] = sorted(interest_topics)
        for topic_id in interest_topics:
            interested[topic_id] = sorted(set(interested.get(topic_id, [])) | {student.id})

    extended_prep = SimpleNamespace(
        node_features=extended_features,
        relation_adjacency=extended_relations,
    )
    node_order, node_index, features, adjacency = _build_matrices(extended_prep, result.config)
    _, embeddings = _forward(features, adjacency, result.weights)
    return embeddings[node_index[student.id]].tolist()


# Scoring signals live in recsys.scoring (shared with the baselines); the
# GraphSAGE ranker keeps the primary-path fallback for empty groups.


def build_graphsage_recommendations(
    dataset: SyntheticDataset,
    hin: HINGraph,
    training_result: GraphSAGETrainingResult,
    top_k: int = 10,
) -> dict[str, list[dict[str, object]]]:
    recommendations: dict[str, list[dict[str, object]]] = {}
    largest_group = max_group_size(dataset)

    for student_id, student in dataset.students.items():
        student_embedding = np.asarray(training_result.embeddings.get(student_id, []), dtype=np.float64)
        if student_embedding.size == 0:
            recommendations[student_id] = []
            continue

        joined_group_ids = set(student.joined_group_ids)
        peer_ids = peer_set(dataset, student_id)
        ranked: list[Recommendation] = []

        for group_id, group in dataset.groups.items():
            # Recommend only student-made groups; academic rooms are scaffolding.
            if group_id in joined_group_ids or not group.is_student_made:
                continue
            group_embedding = np.asarray(training_result.embeddings.get(group_id, []), dtype=np.float64)
            if group_embedding.size == 0:
                continue
            embedding_similarity = cosine_similarity(student_embedding, group_embedding)
            topic_similarity = sparse_cosine_similarity(
                hin.student_topic_weights.get(student_id, {}),
                hin.group_topic_weights.get(group_id, {}),
            )
            path_affinity = study_path_affinity(
                dataset, student_id, group_id, empty_group_uses_primary_path=True
            )
            sem_proximity = semester_proximity(dataset, student_id, group_id)
            group_popularity = popularity(dataset, group_id, largest_group)
            member_similarity = jaccard_similarity(peer_ids, set(group.member_ids))
            serendipity = topic_similarity * (1.0 - member_similarity)
            relevance = (
                (0.65 * embedding_similarity)
                + (0.2 * topic_similarity)
                + (0.1 * path_affinity)
                + (0.05 * sem_proximity)
            )
            score = (
                (0.8 * relevance)
                + (0.15 * serendipity)
                + (0.05 * group_popularity)
            )
            ranked.append(
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

        ranked.sort(key=lambda recommendation: (-recommendation.score, recommendation.group_name))
        recommendations[student_id] = [recommendation.to_dict() for recommendation in ranked[:top_k]]

    return recommendations


def build_graphsage_firestore_payloads(
    dataset: SyntheticDataset,
    hin: HINGraph,
    training_result: GraphSAGETrainingResult,
    top_k: int = 10,
    use_source_student_ids: bool = True,
) -> dict[str, dict[str, object]]:
    recommendations = build_graphsage_recommendations(dataset, hin, training_result, top_k=top_k)
    return {
        (dataset.source_student_id_for(student_id) if use_source_student_ids else student_id): {
            "recommendedRoomIds": [item["groupId"] for item in ranked],
            "recommendationsUpdatedAt": training_result.trained_at_ms,
            "recommendationSource": "GRAPH_SAGE_LOCAL",
        }
        for student_id, ranked in recommendations.items()
    }
