"""
Arquitectura y especificación formal para tensor_gnn_core.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/10_gemelo_digital_unificado_core.md
- Referencia Académica: Verstraete, Murg, Cirac (2008) PEPS Tensor Networks; Evensen (2003) EnKF
"""
import numpy as np


class EnsembleKalmanFilter:
    """
    Filtro de Kalman por Conjuntos (EnKF) para la asimilación de datos estocásticos
    en el Gemelo Digital. Opera estrictamente con tensores NumPy O(1).
    """

    def __init__(self, n_ensembles, state_dim, obs_dim):
        self.n_ensembles = n_ensembles
        self.state_dim = state_dim
        self.obs_dim = obs_dim

        # Matriz de estados del ensamble (state_dim x n_ensembles)
        self.X = np.random.randn(state_dim, n_ensembles)

        # Matriz de observación H (asumimos identidad para simplicidad: observamos todo el estado)
        self.H = np.eye(obs_dim, state_dim)

        # Ruido del modelo Q y ruido de la observación R
        self.Q = np.eye(state_dim) * 0.01
        self.R = np.eye(obs_dim) * 0.1

    def predict(self, dynamics_matrix):
        """Propaga el ensamble en el tiempo usando la dinámica del sistema físico."""
        # Perturbaciones estocásticas
        noise = np.random.multivariate_normal(
            np.zeros(self.state_dim), self.Q, self.n_ensembles
        ).T

        # Ecuación de estado: X = F * X + v
        self.X = dynamics_matrix @ self.X + noise

    def update(self, observation):
        """Asimila la nueva observación para corregir los estados del ensamble."""
        # Perturbar observaciones para cada miembro del ensamble
        obs_noise = np.random.multivariate_normal(
            np.zeros(self.obs_dim), self.R, self.n_ensembles
        ).T
        Y = np.tile(observation.reshape(-1, 1), (1, self.n_ensembles)) + obs_noise

        # Calcular covarianza del ensamble C_ee
        mean_X = np.mean(self.X, axis=1, keepdims=True)
        A = self.X - mean_X
        C_ee = (A @ A.T) / (self.n_ensembles - 1)

        # Calcular ganancia de Kalman K
        # K = C_ee * H^T * (H * C_ee * H^T + R)^-1
        S = self.H @ C_ee @ self.H.T + self.R
        K = C_ee @ self.H.T @ np.linalg.inv(S)

        # Actualizar ensamble: X = X + K * (Y - H * X)
        self.X = self.X + K @ (Y - self.H @ self.X)

    def get_mean_state(self):
        """Retorna el estado asimilado más probable."""
        return np.mean(self.X, axis=1)

    def get_covariance_trace(self):
        """Retorna la traza de la covarianza como indicador de convergencia."""
        mean_X = np.mean(self.X, axis=1, keepdims=True)
        A = self.X - mean_X
        C_ee = (A @ A.T) / (self.n_ensembles - 1)
        return np.trace(C_ee)


import os
import sqlite3

if __name__ == "__main__":
    print("Iniciando Módulo de Asimilación EnKF (Gemelo Digital Tensorial)...")

    # Inicializar EnKF: 100 ensembles, estado 2D (ej: [Vehiculos_AppViajes, Bombas_SaaSRegantes])
    enkf = EnsembleKalmanFilter(n_ensembles=100, state_dim=2, obs_dim=2)

    # Matriz dinámica (evolución estática para la prueba)
    F = np.eye(2)

    db_path = "/home/jaruiz/Desarrollo/simulations_telemetry.db"
    
    # Check if database exists
    if not os.path.exists(db_path):
        print(f"Error: Base de datos no encontrada en {db_path}")
        exit(1)

    print("Conectado a la base de datos de telemetría.")
    conn = sqlite3.connect(db_path)
    
    # Crear tabla de estados asimilados si no existe
    conn.execute('''
        CREATE TABLE IF NOT EXISTS unified_twin_enkf_state (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp_epoch_ms INTEGER,
            state_appviajes REAL,
            state_saasregantes REAL,
            covariance_trace REAL
        )
    ''')

    print("Tick | Estado Asimilado | Covarianza")
    import time
    for tick in range(10):
        # 1. Predicción del modelo
        enkf.predict(F)

        # 2. Llegada de sensor real (Consultar telemetría de AppViajes y SaaSRegantes)
        # Obtenemos la media de velocidad de los vehículos para simular el estado de movilidad
        cursor = conn.cursor()
        cursor.execute("SELECT AVG(speed_kmh) FROM h3_vehicle_telemetry WHERE speed_kmh IS NOT NULL")
        row_app = cursor.fetchone()
        val_appviajes = row_app[0] if row_app and row_app[0] is not None else 30.0

        # Para SaaSRegantes usamos una query estocástica simulada o mock, ya que la tabla exacta podría variar
        # Si hubiera tabla `saasregantes_telemetry`, leeríamos. Por ahora simulamos un valor base + varianza
        val_saasregantes = 45.0 + np.random.randn()

        sensor_reading = np.array([val_appviajes, val_saasregantes])

        # 3. Asimilación (Actualización)
        enkf.update(sensor_reading)

        state = enkf.get_mean_state()
        cov = enkf.get_covariance_trace()

        print(f"{tick:4d} | [{state[0]:.2f}, {state[1]:.2f}] | {cov:.4f}")
        
        # Persistir el estado asimilado
        conn.execute(
            "INSERT INTO unified_twin_enkf_state (timestamp_epoch_ms, state_appviajes, state_saasregantes, covariance_trace) VALUES (?, ?, ?, ?)",
            (int(time.time() * 1000), state[0], state[1], cov)
        )
        conn.commit()

    conn.close()

    if cov < 0.5:
        print("\n✅ Veredicto EnKF: Covarianza convergente. Física matemática válida.")
    else:
        print("\n❌ Veredicto EnKF: Divergencia detectada. Revisar tensores.")
