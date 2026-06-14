"""Additional baselines required by the BA1 Chapter 7 evaluation protocol.

Provides the Random lower bound, the non-personalised Popularity baseline,
and a Node2Vec-style transductive random-walk embedding baseline. Together
with the content-based ranker (``recsys.baselines``), LightGCN
(``recsys.lightgcn_train``) and GraphSAGE (``recsys.graphsage_train``) this
completes the five-baseline comparison defined in BA1 Section 7.3.
"""

from __future__ import annotations

import random
import time
from collections import defaultdict
from dataclasses import asdict, dataclass

import numpy as np

from recsys.models import SyntheticDataset


def build_random_recommendations(
    dataset: SyntheticDataset,
    top_k: int = 10,
    seed: int = 42,
) -> dict[str, list[str]]:
    """Uniform-random ranking over non-joined groups (lower bound)."""
    rng = random.Random(seed)
    group_ids = sorted(dataset.groups)
    ranked: dict[str, list[str]] = {}
    for student_id, student in dataset.students.items():
        joined = set(student.joined_group_ids)
        candidates = [group_id for group_id in group_ids if group_id not in joined]
        rng.shuffle(candidates)
        ranked[student_id] = candidates[:top_k]
    return ranked


def build_popularity_recommendations(
    dataset: SyntheticDataset,
    top_k: int = 10,
) -> dict[str, list[str]]:
    """Most-members-first ranking, identical for every student."""
    by_popularity = sorted(
        dataset.groups.values(),
        key=lambda group: (-len(group.member_ids), group.id),
    )
    ranked: dict[str, list[str]] = {}
    for student_id, student in dataset.students.items():
        joined = set(student.joined_group_ids)
        ranked[student_id] = [
            group.id for group in by_popularity if group.id not in joined
        ][:top_k]
    return ranked


@dataclass(frozen=True)
class Node2VecConfig:
    embedding_dim: int = 16
    walks_per_node: int = 10
    walk_length: int = 20
    window_size: int = 5
    return_param_p: float = 1.0
    in_out_param_q: float = 1.0
    seed: int = 42


@dataclass
class Node2VecTrainingResult:
    config: Node2VecConfig
    node_embeddings: dict[str, list[float]]
    num_walks: int
    trained_at_ms: int

    def summary(self) -> dict[str, object]:
        return {
            "config": asdict(self.config),
            "numEmbeddings": len(self.node_embeddings),
            "numWalks": self.num_walks,
            "trainedAtMs": self.trained_at_ms,
        }


def _membership_adjacency(dataset: SyntheticDataset) -> dict[str, list[str]]:
    adjacency: dict[str, list[str]] = defaultdict(list)
    for student in dataset.students.values():
        for group_id in student.joined_group_ids:
            adjacency[student.id].append(group_id)
            adjacency[group_id].append(student.id)
    return {node: sorted(neighbors) for node, neighbors in adjacency.items()}


def _biased_walks(
    adjacency: dict[str, list[str]],
    config: Node2VecConfig,
    rng: random.Random,
) -> list[list[str]]:
    walks: list[list[str]] = []
    nodes = sorted(adjacency)
    for _ in range(config.walks_per_node):
        rng.shuffle(nodes)
        for start in nodes:
            walk = [start]
            while len(walk) < config.walk_length:
                current = walk[-1]
                neighbors = adjacency.get(current, [])
                if not neighbors:
                    break
                if len(walk) == 1 or (config.return_param_p == 1.0 and config.in_out_param_q == 1.0):
                    walk.append(rng.choice(neighbors))
                    continue
                previous = walk[-2]
                previous_neighbors = set(adjacency.get(previous, []))
                weights = []
                for candidate in neighbors:
                    if candidate == previous:
                        weights.append(1.0 / config.return_param_p)
                    elif candidate in previous_neighbors:
                        weights.append(1.0)
                    else:
                        weights.append(1.0 / config.in_out_param_q)
                walk.append(rng.choices(neighbors, weights=weights, k=1)[0])
            walks.append(walk)
    return walks


def train_node2vec_embeddings(
    dataset: SyntheticDataset,
    config: Node2VecConfig | None = None,
) -> Node2VecTrainingResult:
    """Random-walk co-occurrence embedding over the student-group bipartite graph.

    Walks are generated with node2vec's second-order bias (p, q); embeddings are
    obtained by factorising the shifted positive PMI co-occurrence matrix with a
    truncated SVD, which is equivalent to skip-gram with negative sampling
    (Levy and Goldberg 2014) while remaining fast and fully deterministic.
    """
    config = config or Node2VecConfig()
    rng = random.Random(config.seed)
    adjacency = _membership_adjacency(dataset)
    walks = _biased_walks(adjacency, config, rng)

    node_order = sorted(adjacency)
    node_index = {node_id: idx for idx, node_id in enumerate(node_order)}
    size = len(node_order)
    cooccurrence = np.zeros((size, size), dtype=np.float64)

    for walk in walks:
        indices = [node_index[node_id] for node_id in walk]
        for center_pos, center_idx in enumerate(indices):
            lo = max(0, center_pos - config.window_size)
            hi = min(len(indices), center_pos + config.window_size + 1)
            for context_pos in range(lo, hi):
                if context_pos == center_pos:
                    continue
                cooccurrence[center_idx, indices[context_pos]] += 1.0

    total = cooccurrence.sum()
    if total == 0.0:
        embeddings = {node_id: [0.0] * config.embedding_dim for node_id in node_order}
        return Node2VecTrainingResult(config, embeddings, len(walks), int(time.time() * 1000))

    row_sums = cooccurrence.sum(axis=1, keepdims=True)
    col_sums = cooccurrence.sum(axis=0, keepdims=True)
    row_sums[row_sums == 0.0] = 1.0
    col_sums[col_sums == 0.0] = 1.0
    with np.errstate(divide="ignore"):
        pmi = np.log((cooccurrence * total) / (row_sums * col_sums))
    ppmi = np.where(np.isfinite(pmi), np.maximum(pmi, 0.0), 0.0)

    left, singular_values, _ = np.linalg.svd(ppmi, full_matrices=False)
    dim = min(config.embedding_dim, len(singular_values))
    vectors = left[:, :dim] * np.sqrt(singular_values[:dim])
    if dim < config.embedding_dim:
        vectors = np.pad(vectors, ((0, 0), (0, config.embedding_dim - dim)))

    embeddings = {
        node_id: vectors[node_index[node_id]].tolist()
        for node_id in node_order
    }
    return Node2VecTrainingResult(config, embeddings, len(walks), int(time.time() * 1000))


def build_node2vec_recommendations(
    dataset: SyntheticDataset,
    training_result: Node2VecTrainingResult,
    top_k: int = 10,
) -> dict[str, list[str]]:
    """Cosine-similarity ranking with a popularity fallback for unseen students.

    Like LightGCN, Node2Vec is transductive: students without membership edges
    at training time receive no embedding and fall back to the popularity
    ranking, which is exactly the cold-start weakness under study.
    """
    popularity_ranked = build_popularity_recommendations(dataset, top_k=top_k)
    ranked: dict[str, list[str]] = {}

    for student_id, student in dataset.students.items():
        embedding = np.asarray(
            training_result.node_embeddings.get(student_id, []), dtype=np.float64
        )
        if embedding.size == 0 or float(np.linalg.norm(embedding)) == 0.0:
            ranked[student_id] = popularity_ranked[student_id]
            continue

        joined = set(student.joined_group_ids)
        scored: list[tuple[float, str]] = []
        for group_id in dataset.groups:
            if group_id in joined:
                continue
            group_embedding = np.asarray(
                training_result.node_embeddings.get(group_id, []), dtype=np.float64
            )
            denominator = np.linalg.norm(embedding) * np.linalg.norm(group_embedding)
            score = float(np.dot(embedding, group_embedding) / denominator) if denominator else 0.0
            scored.append((score, group_id))
        scored.sort(key=lambda item: (-item[0], item[1]))
        ranked[student_id] = [group_id for _, group_id in scored[:top_k]]

    return ranked
