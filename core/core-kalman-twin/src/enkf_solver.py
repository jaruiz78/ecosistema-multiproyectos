"""
Motor de Asimilación Estocástica de Datos: Ensemble Kalman Filter (EnKF).
Implementación vectorizada de alto rendimiento O(N) con Factorización Cholesky y Regularización Tikhonov.

Referencias Teóricas:
- Evensen, G. (2003). The Ensemble Kalman Filter: theoretical formulation and practical implementation. Ocean Dynamics.
- Burgers, G., Jan van Leeuwen, P., & Evensen, G. (1998). Analysis scheme in the ensemble Kalman filter.
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-digital-twin-peps-enkf.md
- Curriculum: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_matematicas_y_fisica/01_asimilacion_datos_enkf.md
"""

from typing import Callable, Optional
import numpy as np
import numpy.typing as npt
from scipy.linalg import cho_factor, cho_solve


class EnsembleKalmanFilter:
    r"""
    Filtro de Kalman por Ensambles (EnKF) para la estimación de estado estocástico en sistemas no lineales.

    Ecuaciones Gobernantes:
    1. Propagación temporal:
       \[ E_{k|k-1} = F \cdot E_{k-1} + W, \quad W \sim \mathcal{N}(0, Q) \]
    2. Asimilación observacional robusta con Cholesky:
       \[ Z = z \mathbf{1}^T + V, \quad V \sim \mathcal{N}(0, R) \]
       \[ A = E - \bar{E} \]
       \[ S = \frac{1}{N-1} (H A)(H A)^T + R + \epsilon I \]
       \[ K = \frac{1}{N-1} A (H A)^T S^{-1} \quad \text{mediante Cholesky } S = L L^T \]
       \[ E_{k|k} = E + K (Z - H E) \]
    """

    def __init__(
        self,
        ensemble_size: int,
        state_dim: int,
        obs_dim: int,
        seed: Optional[int] = None
    ) -> None:
        r"""
        Inicializa el ensamble de estado.

        :param ensemble_size: Número de miembros del ensamble $N$ ($N \ge 2$)
        :param state_dim: Dimensión del vector de estado $n$
        :param obs_dim: Dimensión del vector de observaciones $m$
        :param seed: Semilla para inicialización pseudoaleatoria determinista
        """
        if ensemble_size < 2:
            raise ValueError("ensemble_size debe ser mayor o igual a 2 para calcular la covarianza muestral.")
        if state_dim < 1 or obs_dim < 1:
            raise ValueError("Las dimensiones de estado y observación deben ser estrictamente positivas.")

        self.N: int = ensemble_size
        self.n: int = state_dim
        self.m: int = obs_dim
        self.rng: np.random.Generator = np.random.default_rng(seed)
        self.E: npt.NDArray[np.float64] = self.rng.standard_normal((self.n, self.N))

    def predict(
        self,
        F: npt.NDArray[np.float64],
        Q: npt.NDArray[np.float64]
    ) -> None:
        r"""
        Avanza el estado del ensamble según el modelo dinámico lineal $F$ y la covarianza de ruido $Q$.
        """
        noise = self.rng.multivariate_normal(np.zeros(self.n), Q, self.N).T
        self.E = F @ self.E + noise

    def predict_nonlinear(
        self,
        f: Callable[[npt.NDArray[np.float64]], npt.NDArray[np.float64]],
        Q: npt.NDArray[np.float64]
    ) -> None:
        r"""
        Avanza el estado del ensamble aplicando un operador no lineal $f(x)$ a cada miembro del ensamble.
        """
        propagated = f(self.E)
        noise = self.rng.multivariate_normal(np.zeros(self.n), Q, self.N).T
        self.E = propagated + noise

    def update(
        self,
        z: npt.NDArray[np.float64],
        H: npt.NDArray[np.float64],
        R: npt.NDArray[np.float64],
        tikhonov_epsilon: float = 1e-7
    ) -> None:
        r"""
        Asimila una observación lineal $z$ mediante factorización de Cholesky numéricamente estable.
        """
        V = self.rng.multivariate_normal(np.zeros(self.m), R, self.N).T
        Z = np.tile(z.reshape(-1, 1), (1, self.N)) + V

        e_mean = np.mean(self.E, axis=1, keepdims=True)
        A = self.E - e_mean

        HA = H @ A
        # Matriz de innovación con regularización de Tikhonov
        S = (HA @ HA.T) / (self.N - 1) + R + np.eye(self.m) * tikhonov_epsilon

        # Resolución numéricamente estable de Ganancia de Kalman vía Cholesky
        try:
            c, lower = cho_factor(S, lower=True)
            # K = (1/(N-1)) * A * (HA)^T * S^-1 => K S = (1/(N-1)) * A * (HA)^T
            rhs = (A @ HA.T) / (self.N - 1)
            # Resolver S^T K^T = rhs^T (S es simétrica)
            K = cho_solve((c, lower), rhs.T).T
        except Exception:
            # Fallback seguro con pseudoinversa si la matriz de covarianza es cuasi-singular
            K = (A @ HA.T) / (self.N - 1) @ np.linalg.pinv(S)

        self.E = self.E + K @ (Z - H @ self.E)

    def get_state_mean(self) -> npt.NDArray[np.float64]:
        r"""
        Retorna la media del ensamble $\bar{x} \in \mathbb{R}^n$.
        """
        return np.mean(self.E, axis=1)

    def get_covariance_trace(self) -> float:
        r"""
        Retorna la traza de la matriz de covarianza muestral $\operatorname{Tr}(P) = \sum \sigma_i^2$.
        """
        e_mean = np.mean(self.E, axis=1, keepdims=True)
        A = self.E - e_mean
        P = (A @ A.T) / (self.N - 1)
        return float(np.trace(P))
