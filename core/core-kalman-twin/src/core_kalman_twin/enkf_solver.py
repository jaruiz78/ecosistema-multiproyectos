#!/usr/bin/env python3
"""
Arquitectura y especificación formal para enkf_solver.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/03_asimilacion_de_datos_enkf.md
- Referencia Académica: Evensen (2003) Sequential Data Assimilation with EnKF (JGR); Myers & Tapley (1976) Adaptive Sequential Estimation
"""
import numpy as np

class EnKFSolver:
    def __init__(self, n_states: int = 10, process_noise: float = 1e-4, measurement_noise: float = 1e-2):
        self.n_states = n_states
        self.q = process_noise
        self.r = measurement_noise
        self.P = np.eye(n_states, dtype=np.float64) * 1.0  # Covarianza inicial
        self.state = np.zeros(n_states, dtype=np.float64)  # Estado estimado

    def update(self, measurement: np.ndarray) -> np.ndarray:
        """
        Ejecuta la actualización de Kalman numéricamente estable:
          P^f = P^{a-1} + Q
          K = P^f (P^f + R)^{-1}  [Calculado vía np.linalg.solve sin inversión explícita]
          x^a = x^f + K (y - x^f)
          P^a = (I - K) P^f (I - K)^T + K R K^T  [Forma Joseph numéricamente simétrica]
        """
        measurement = np.asarray(measurement, dtype=np.float64)
        I = np.eye(self.n_states, dtype=np.float64)
        
        # 1. Predicción a priori de covarianza
        P_f = self.P + I * self.q
        
        # 2. Innovación y matriz de covarianza de innovación
        S = P_f + I * self.r
        
        # 3. Ganancia de Kalman vía resolución lineal: S^T K^T = P_f^T => K = (np.linalg.solve(S, P_f))^T
        K = np.linalg.solve(S.T, P_f.T).T
        
        # 4. Actualización de estado
        residual = measurement - self.state
        self.state = self.state + K @ residual
        
        # 5. Actualización de covarianza mediante forma Joseph (garantiza simetría y definición positiva)
        I_minus_K = I - K
        R_mat = I * self.r
        self.P = I_minus_K @ P_f @ I_minus_K.T + K @ R_mat @ K.T
        
        # Enforce exact symmetry
        self.P = 0.5 * (self.P + self.P.T)
        
        return self.state

    def update_adaptive(self, measurement: np.ndarray, alpha: float = 0.1) -> np.ndarray:
        """
        Ejecuta la actualización de Kalman con Auto-Tuning adaptativo de Myers-Tapley:
        Ajusta dinámicamente la covarianza de ruido R basándose en los residuales
        de innovación nu = (y - x^f).
        """
        measurement = np.asarray(measurement, dtype=np.float64)
        I = np.eye(self.n_states, dtype=np.float64)
        
        # 1. Predicción a priori
        P_f = self.P + I * self.q
        
        # 2. Residual de innovación
        residual = measurement - self.state
        
        # 3. Estimador adaptativo Myers-Tapley para ruido de medición R
        innovation_var = float(np.mean(residual ** 2))
        estimated_r = max(1e-4, innovation_var - float(np.trace(P_f) / self.n_states))
        self.r = (1.0 - alpha) * self.r + alpha * estimated_r
        
        # 4. Ganancia de Kalman con R adaptada
        S = P_f + I * self.r
        K = np.linalg.solve(S.T, P_f.T).T
        
        # 5. Actualización de estado
        self.state = self.state + K @ residual
        
        # 6. Actualización Joseph-form
        I_minus_K = I - K
        R_mat = I * self.r
        self.P = I_minus_K @ P_f @ I_minus_K.T + K @ R_mat @ K.T
        self.P = 0.5 * (self.P + self.P.T)
        
        return self.state

    def get_variance(self) -> float:
        return float(np.trace(self.P) / self.n_states)

    def get_measurement_noise(self) -> float:
        return float(self.r)
