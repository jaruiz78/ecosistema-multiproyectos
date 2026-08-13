#!/usr/bin/env python3
"""
Módulo Autónomo de Asimilación de Datos mediante Filtro de Kalman Ensemble (EnKF).
Centralizado en core-kalman-twin para reutilización en todo el ecosistema.
"""
import numpy as np

class EnKFSolver:
    def __init__(self, n_states: int = 10, process_noise: float = 1e-4, measurement_noise: float = 1e-2):
        self.n_states = n_states
        self.q = process_noise
        self.r = measurement_noise
        self.P = np.eye(n_states) * 1.0  # Covarianza inicial
        self.state = np.zeros(n_states)  # Estado estimado

    def update(self, measurement: np.ndarray) -> np.ndarray:
        """
        Ejecuta la actualización de Kalman:
          P^f = P^{a-1} + q
          K = P^f / (P^f + r)
          P^a = (1 - K) P^f
        """
        # Predicción
        P_f = self.P + np.eye(self.n_states) * self.q
        
        # Ganancia de Kalman
        K = P_f @ np.linalg.inv(P_f + np.eye(self.n_states) * self.r)
        
        # Actualización de estado
        residual = measurement - self.state
        self.state = self.state + K @ residual
        
        # Actualización de covarianza
        self.P = (np.eye(self.n_states) - K) @ P_f
        
        return self.state

    def get_variance(self) -> float:
        return float(np.trace(self.P) / self.n_states)
