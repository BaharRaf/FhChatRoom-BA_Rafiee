"""Finite-difference audits of both hand-derived backward passes.

Both models compute their gradients by hand, and one of them was wrong: the
GraphSAGE backward pass evaluated the loss on row-normalised embeddings but
propagated the incoming gradient straight into the tanh derivative, omitting
the Jacobian of that normalisation. The faulty gradient was still a *descent*
direction, so the training loss fell as usual and nothing in the loss curve
revealed the defect -- it only surfaced when the gradient was checked against
finite differences.

These tests are the committed form of that audit. They pin the numbers quoted
in the thesis (Section 3.4) and guard against the defect returning:

* the corrected GraphSAGE gradient matches central differences to ~1e-9,
* the LightGCN gradient does too, so the comparison does not rest on an
  unverified gradient on either side,
* the historical bug is reproduced explicitly and shown to be badly
  misaligned (cosine ~0.6), which is what makes the thesis claim auditable
  rather than merely asserted.
"""

from __future__ import annotations

import numpy as np

from recsys.graphsage_train import _backward as graphsage_backward
from recsys.graphsage_train import _forward
from recsys.graphsage_train import _init_weight
from recsys.graphsage_train import _normalize_rows
from recsys.graphsage_train import _ranking_loss_and_gradient
from recsys.lightgcn_train import _backward as lightgcn_backward
from recsys.lightgcn_train import _bpr_loss_and_gradients
from recsys.lightgcn_train import _propagate

EPSILON = 1e-6


def _relative_error(analytic: np.ndarray, numerical: np.ndarray) -> float:
    denominator = np.linalg.norm(analytic) + np.linalg.norm(numerical)
    return float(np.linalg.norm(analytic - numerical) / max(denominator, 1e-12))


def _cosine(analytic: np.ndarray, numerical: np.ndarray) -> float:
    scale = np.linalg.norm(analytic) * np.linalg.norm(numerical)
    return float(analytic.ravel() @ numerical.ravel() / max(scale, 1e-12))


def _graphsage_fixture(seed: int = 0, num_layers: int = 2):
    rng = np.random.default_rng(seed)
    num_nodes, feature_dim = 12, 5

    features = _normalize_rows(rng.normal(size=(num_nodes, feature_dim)))
    adjacency = np.abs(rng.normal(size=(num_nodes, num_nodes)))
    np.fill_diagonal(adjacency, 0.0)
    adjacency /= adjacency.sum(axis=1, keepdims=True)

    dimensions = [6] * (num_layers - 1) + [4]
    weights = []
    current_dim = feature_dim
    for output_dim in dimensions:
        weights.append(_init_weight(rng, current_dim * 2, output_dim))
        current_dim = output_dim

    triplets = np.array([[0, 5, 9], [1, 6, 10], [2, 7, 11], [3, 8, 5]], dtype=np.int64)
    return features, adjacency, weights, triplets


def _graphsage_loss(features, adjacency, weights, triplets) -> float:
    _, embeddings = _forward(features, adjacency, weights)
    loss, _ = _ranking_loss_and_gradient(embeddings, triplets)
    return loss


def _graphsage_numerical(features, adjacency, weights, triplets, weight_decay):
    numerical = []
    for weight in weights:
        gradient = np.zeros_like(weight)
        for row in range(weight.shape[0]):
            for column in range(weight.shape[1]):
                original = weight[row, column]
                weight[row, column] = original + EPSILON
                plus = _graphsage_loss(features, adjacency, weights, triplets)
                weight[row, column] = original - EPSILON
                minus = _graphsage_loss(features, adjacency, weights, triplets)
                weight[row, column] = original
                gradient[row, column] = (plus - minus) / (2 * EPSILON)
        # Finite differences see only the loss; add the weight-decay term.
        numerical.append(gradient + weight_decay * weight)
    return numerical


def _graphsage_analytic(features, adjacency, weights, triplets, weight_decay):
    caches, embeddings = _forward(features, adjacency, weights)
    _, grad_embeddings = _ranking_loss_and_gradient(embeddings, triplets)
    return graphsage_backward(
        features=features,
        adjacency=adjacency,
        weights=weights,
        caches=caches,
        grad_output=grad_embeddings,
        weight_decay=weight_decay,
    )


def test_graphsage_gradient_matches_finite_differences():
    """Two layers, with and without weight decay."""
    for weight_decay in (0.0, 1e-4):
        features, adjacency, weights, triplets = _graphsage_fixture()
        analytic = _graphsage_analytic(features, adjacency, weights, triplets, weight_decay)
        numerical = _graphsage_numerical(features, adjacency, weights, triplets, weight_decay)
        for layer, (a, n) in enumerate(zip(analytic, numerical)):
            assert _relative_error(a, n) < 1e-5, f"layer {layer}, decay {weight_decay}"
            assert _cosine(a, n) > 1 - 1e-9, f"layer {layer}, decay {weight_decay}"


def test_graphsage_gradient_holds_for_single_layer():
    features, adjacency, weights, triplets = _graphsage_fixture(num_layers=1)
    analytic = _graphsage_analytic(features, adjacency, weights, triplets, 0.0)
    numerical = _graphsage_numerical(features, adjacency, weights, triplets, 0.0)
    assert _relative_error(analytic[0], numerical[0]) < 1e-5


def test_graphsage_gradient_holds_under_tanh_saturation():
    """Large weights push tanh to +/-1, the regime the defect produced."""
    features, adjacency, weights, triplets = _graphsage_fixture()
    weights = [weight * 40.0 for weight in weights]
    analytic = _graphsage_analytic(features, adjacency, weights, triplets, 0.0)
    numerical = _graphsage_numerical(features, adjacency, weights, triplets, 0.0)
    for a, n in zip(analytic, numerical):
        assert _relative_error(a, n) < 1e-5


def test_graphsage_gradient_holds_with_a_zero_feature_row():
    """Exercises the zero-norm guard shared with _normalize_rows."""
    features, adjacency, weights, triplets = _graphsage_fixture()
    features[4] = 0.0
    analytic = _graphsage_analytic(features, adjacency, weights, triplets, 0.0)
    numerical = _graphsage_numerical(features, adjacency, weights, triplets, 0.0)
    for a, n in zip(analytic, numerical):
        assert _relative_error(a, n) < 1e-5


def _historical_backward_without_jacobian(
    features, adjacency, weights, caches, grad_output
):
    """The backward pass exactly as it stood before the correction.

    Reproduced verbatim rather than derived from the fixed version: it feeds
    dL/d(unit) straight into the tanh derivative, with no
    (I - u u^T)/||x|| pull-back.
    """
    gradients = [np.zeros_like(weight) for weight in weights]
    current_grad = grad_output

    for layer_index in reversed(range(len(weights))):
        weight = weights[layer_index]
        previous = (
            features
            if layer_index == 0
            else np.tanh(caches[layer_index - 1]["pre_activation"])
        )
        pre_activation = caches[layer_index]["pre_activation"]
        layer_input = caches[layer_index]["input"]

        activation_grad = current_grad * (1.0 - np.tanh(pre_activation) ** 2)
        gradients[layer_index] = layer_input.T @ activation_grad

        grad_input = activation_grad @ weight.T
        previous_dim = previous.shape[1]
        grad_self = grad_input[:, :previous_dim]
        grad_neighbors = grad_input[:, previous_dim:]
        current_grad = grad_self + adjacency.T @ grad_neighbors

    return gradients


def test_omitting_the_normalisation_jacobian_misaligns_the_gradient():
    """Reproduces the historical defect and pins how wrong it was.

    Skipping the (I - u u^T)/||x|| pull-back is what the backward pass used to
    do. The resulting direction still has positive overlap with the true
    gradient -- which is why training loss still fell and the bug stayed
    hidden -- but it is far from correct. The thesis quotes cosine 0.60-0.65;
    this test holds that interval.
    """
    features, adjacency, weights, triplets = _graphsage_fixture()
    caches, embeddings = _forward(features, adjacency, weights)
    _, grad_embeddings = _ranking_loss_and_gradient(embeddings, triplets)

    buggy = _historical_backward_without_jacobian(
        features, adjacency, weights, caches, grad_embeddings
    )
    numerical = _graphsage_numerical(features, adjacency, weights, triplets, 0.0)

    cosines = [_cosine(b, n) for b, n in zip(buggy, numerical)]
    # Quoted in the thesis as 0.60-0.65; bounded generously so the test pins
    # the finding rather than the exact fixture.
    assert all(0.55 <= c <= 0.70 for c in cosines), cosines
    # Still a descent direction -- positive overlap is precisely why the
    # training loss kept falling and the defect stayed invisible.
    assert all(c > 0.0 for c in cosines), cosines
    # And the corrected path is unambiguously better on the same fixture.
    corrected = _graphsage_analytic(features, adjacency, weights, triplets, 0.0)
    assert all(_cosine(c, n) > 0.999 for c, n in zip(corrected, numerical))


def _lightgcn_fixture(seed: int = 0):
    rng = np.random.default_rng(seed)
    num_students, num_groups, dim, num_layers = 9, 7, 4, 2

    interaction = (rng.random((num_students, num_groups)) < 0.4).astype(np.float64)
    interaction[interaction.sum(axis=1) == 0, 0] = 1.0
    norm = np.sqrt(interaction.sum(axis=1, keepdims=True)) * np.sqrt(
        np.maximum(interaction.sum(axis=0, keepdims=True), 1.0)
    )
    s2g = interaction / np.where(norm > 0, norm, 1.0)

    student_embeddings = rng.normal(scale=0.3, size=(num_students, dim))
    group_embeddings = rng.normal(scale=0.3, size=(num_groups, dim))
    triplets = np.array([[0, 1, 3], [2, 0, 5], [4, 2, 6], [7, 3, 1]], dtype=np.int64)
    return student_embeddings, group_embeddings, s2g, s2g.T, num_layers, triplets


def test_lightgcn_gradient_matches_finite_differences():
    """The baseline's backward pass is hand-derived too, so it is audited too."""
    students, groups, s2g, g2s, num_layers, triplets = _lightgcn_fixture()

    def loss_of():
        _, _, final_students, final_groups = _propagate(
            students, groups, s2g, g2s, num_layers
        )
        loss, _, _ = _bpr_loss_and_gradients(final_students, final_groups, triplets)
        return loss

    student_layers, group_layers, final_students, final_groups = _propagate(
        students, groups, s2g, g2s, num_layers
    )
    _, grad_students, grad_groups = _bpr_loss_and_gradients(
        final_students, final_groups, triplets
    )
    analytic_students, analytic_groups = lightgcn_backward(
        student_layers, group_layers, grad_students, grad_groups, s2g, g2s
    )

    for table, analytic in ((students, analytic_students), (groups, analytic_groups)):
        numerical = np.zeros_like(table)
        for row in range(table.shape[0]):
            for column in range(table.shape[1]):
                original = table[row, column]
                table[row, column] = original + EPSILON
                plus = loss_of()
                table[row, column] = original - EPSILON
                minus = loss_of()
                table[row, column] = original
                numerical[row, column] = (plus - minus) / (2 * EPSILON)
        assert _relative_error(analytic, numerical) < 1e-5
        assert _cosine(analytic, numerical) > 1 - 1e-9
