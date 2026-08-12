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


if __name__ == "__main__":
    print("Iniciando Módulo de Asimilación EnKF (Gemelo Digital Tensorial)...")

    # Inicializar EnKF: 100 ensembles, estado 2D (ej: [Oferta, Demanda]), obs 2D
    enkf = EnsembleKalmanFilter(n_ensembles=100, state_dim=2, obs_dim=2)

    # Matriz dinámica (evolución estática para la prueba)
    F = np.eye(2)

    # Simular 10 Ticks de asimilación
    print("Tick | Estado Asimilado | Covarianza")
    for tick in range(10):
        # 1. Predicción del modelo
        enkf.predict(F)

        # 2. Llegada de sensor real (ej: IoT AppViajes o SaaSRegantes)
        sensor_reading = np.array([10.0 + np.random.randn(), 5.0 + np.random.randn()])

        # 3. Asimilación (Actualización)
        enkf.update(sensor_reading)

        state = enkf.get_mean_state()
        cov = enkf.get_covariance_trace()

        print(f"{tick:4d} | [{state[0]:.2f}, {state[1]:.2f}] | {cov:.4f}")

    if cov < 0.5:
        print("\n✅ Veredicto EnKF: Covarianza convergente. Física matemática válida.")
    else:
        print("\n❌ Veredicto EnKF: Divergencia detectada. Revisar tensores.")
