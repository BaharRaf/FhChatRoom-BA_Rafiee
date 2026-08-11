"""Differential-privacy utilities for the GraphSAGE pipeline.

Implements the meta-path-sensitivity-tiered Gaussian mechanism designed in
BA1 Chapter 5 (inspired by HeteDP, Wei et al. 2022): noise is injected into
the row-normalised neighbourhood-aggregation matrices before GNN training.
Relations that directly encode group membership receive the strongest noise,
while low-sensitivity topical relations retain more utility.

The mechanism perturbs each relation aggregation matrix once before training
(input perturbation). Because every subsequent training step is
post-processing of the noised aggregations, the privacy guarantee is
determined by this single application of the Gaussian mechanism per relation.
Relations partition the edge set, so parallel composition applies across
relations: the configured epsilon is the budget of the most sensitive
MEMBER_OF tier (which receives the full calibrated noise), while lower tiers
receive less noise and therefore consume a larger effective epsilon, so the
composed edge-level guarantee across all relations is governed by the
least-noised tier (about epsilon/0.3 with the default multipliers). The
classical Gaussian calibration used here is proven for epsilon < 1; larger
budgets use it as a nominal calibration (see thesis Section 3.6).
"""

from __future__ import annotations

import math
from dataclasses import dataclass, field

import numpy as np


# Meta-path sensitivity tiers from BA1 Section 5.2:
#   Student -> Group           : high   (directly encodes membership)
#   Student -> Message -> Group: medium (indirectly reveals presence)
#   Student -> Message -> Topic: low    (topical interest only)
DEFAULT_RELATION_TIERS: dict[str, str] = {
    "MEMBER_OF": "high",
    # A friendship directly identifies a social tie between two students and
    # is at least as disclosive as a group membership, so it sits in the
    # high-sensitivity tier and receives the full calibrated noise.
    "FRIENDS_WITH": "high",
    "SENDS": "medium",
    "POSTED_IN": "medium",
    "CONTAINS": "medium",
    "INTERESTED_IN": "low",
    "RELATED_TO": "low",
}

# Fraction of the base noise scale applied per tier. The high tier always
# receives the full calibrated noise; lower tiers receive proportionally less
# (i.e. enjoy a larger effective epsilon), mirroring HeteDP's budget split.
DEFAULT_TIER_NOISE_MULTIPLIERS: dict[str, float] = {
    "high": 1.0,
    "medium": 0.6,
    "low": 0.3,
}


@dataclass(frozen=True)
class DPConfig:
    """Configuration for the tiered Gaussian aggregation-perturbation mechanism.

    The L2 sensitivity of one row of a row-normalised aggregation matrix with
    respect to the addition or removal of a single edge is bounded by
    sqrt(2)/d for a node of degree d. The trainer enforces the bound: when DP
    is active, every relation row is deterministically subsampled to at most
    ``degree_bound`` neighbours before noise is applied (see
    ``graphsage_train._cap_neighbors``), so the effective sensitivity is
    sqrt(2)/degree_bound. Nodes with degree below the bound have *higher*
    per-edge sensitivity (sqrt(2)/d > sqrt(2)/degree_bound); this residual
    risk is discussed as a limitation in the thesis.
    """

    epsilon: float = 5.0
    delta: float = 1e-5
    degree_bound: int = 15
    relation_tiers: dict[str, str] = field(default_factory=lambda: dict(DEFAULT_RELATION_TIERS))
    tier_noise_multipliers: dict[str, float] = field(
        default_factory=lambda: dict(DEFAULT_TIER_NOISE_MULTIPLIERS)
    )
    seed: int = 42

    @property
    def sensitivity(self) -> float:
        return math.sqrt(2.0) / max(self.degree_bound, 1)

    def to_dict(self) -> dict[str, object]:
        return {
            "epsilon": self.epsilon,
            "delta": self.delta,
            "degreeBound": self.degree_bound,
            "sensitivity": round(self.sensitivity, 6),
            "relationTiers": dict(self.relation_tiers),
            "tierNoiseMultipliers": dict(self.tier_noise_multipliers),
            "seed": self.seed,
            "baseSigma": round(self.base_sigma(), 6),
        }

    def base_sigma(self) -> float:
        """Gaussian-mechanism noise scale for the configured (epsilon, delta).

        Uses the classical calibration sigma >= sqrt(2 ln(1.25/delta)) * S / epsilon
        (Dwork and Roth 2014, Theorem A.1).
        """
        if self.epsilon <= 0.0:
            raise ValueError("epsilon must be positive")
        return math.sqrt(2.0 * math.log(1.25 / self.delta)) * self.sensitivity / self.epsilon

    def sigma_for_relation(self, relation: str) -> float:
        tier = self.relation_tiers.get(relation, "high")
        multiplier = self.tier_noise_multipliers.get(tier, 1.0)
        return self.base_sigma() * multiplier


@dataclass(frozen=True)
class GradientDPConfig:
    """Configuration for DP-SGD-style gradient perturbation (BA1 Section 5.2).

    Each training step clips every weight-gradient matrix to L2 norm
    ``clip_norm`` (g <- g * min(1, C/||g||_2)) and adds Gaussian noise.
    Because the *aggregate* (full-batch) gradient is clipped rather than
    per-example gradients, the worst-case difference between the clipped
    gradients of two neighbouring datasets is bounded by 2C (both vectors
    have norm at most C), so the per-step L2 sensitivity is 2 * clip_norm.
    Releasing T clipped gradients is equivalent to one Gaussian mechanism on
    the concatenated gradient vector, whose L2 sensitivity is bounded by
    sqrt(T) * 2C, so the noise scale calibrated for the full training run is

        sigma = sqrt(2 ln(1.25/delta)) * sqrt(T) * 2C / epsilon.

    This conservative closed-form accounting follows Dwork and Roth (2014,
    Theorem A.1) applied to the composed release; per-example clipping and a
    moments-accountant bound (Abadi et al. 2016) would both be tighter and
    are noted as future work.
    """

    epsilon: float = 5.0
    delta: float = 1e-5
    clip_norm: float = 1.0
    total_steps: int = 150
    seed: int = 42

    def sigma(self) -> float:
        if self.epsilon <= 0.0:
            raise ValueError("epsilon must be positive")
        return (
            math.sqrt(2.0 * math.log(1.25 / self.delta))
            * math.sqrt(max(self.total_steps, 1))
            * 2.0
            * self.clip_norm
            / self.epsilon
        )

    def to_dict(self) -> dict[str, object]:
        return {
            "mechanism": "gradient",
            "epsilon": self.epsilon,
            "delta": self.delta,
            "clipNorm": self.clip_norm,
            "totalSteps": self.total_steps,
            "seed": self.seed,
            "sigma": round(self.sigma(), 6),
        }


def clip_and_noise_gradient(
    gradient: np.ndarray,
    config: GradientDPConfig,
    rng: np.random.Generator,
) -> np.ndarray:
    """Clips a gradient matrix to ``clip_norm`` and adds calibrated Gaussian noise.

    The noise scale already accounts for composition over the full training
    run (see :class:`GradientDPConfig`), so this function can be applied once
    per step without further bookkeeping.
    """
    norm = float(np.linalg.norm(gradient))
    if norm > config.clip_norm and norm > 0.0:
        gradient = gradient * (config.clip_norm / norm)
    return gradient + rng.normal(0.0, config.sigma(), size=gradient.shape)


def perturb_relation_matrix(
    matrix: np.ndarray,
    relation: str,
    config: DPConfig,
    rng: np.random.Generator,
) -> np.ndarray:
    """Adds tiered Gaussian noise to a row-normalised relation aggregation matrix.

    Negative entries produced by the noise are clipped to zero and rows are
    re-normalised; both operations are post-processing and do not weaken the
    differential-privacy guarantee.
    """
    sigma = config.sigma_for_relation(relation)
    if sigma <= 0.0:
        return matrix

    noised = matrix + rng.normal(0.0, sigma, size=matrix.shape)
    noised = np.clip(noised, 0.0, None)
    row_sums = noised.sum(axis=1, keepdims=True)
    row_sums[row_sums == 0.0] = 1.0
    return noised / row_sums
