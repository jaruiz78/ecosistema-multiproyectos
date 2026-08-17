# 🥋 Kata 11: Gemelo Digital, Redes Tensoriales PEPS y Asimilación EnKF

---

## 🏛️ 1. Ancla Mental y Analogía Isomórfica (El Test de los 12 Años)

> **Analogía**: Imagina que intentas predecir la posición exacta de un barco en medio de una tormenta sin GPS directo.
> - **El modelo físico**: Sabes la velocidad del barco y el viento, pero las olas lo empujan al azar.
> - **Las observaciones con ruido**: Cada 5 minutos ves un destello de un faro a lo lejos, pero la niebla distorsiona la distancia.
> - **El Filtro de Kalman por Ensambles (EnKF)**: En lugar de simular un solo barco, simulas una "nube" de 100 barcos virtuales. Cuando llega el destello del faro, comparas la nube con la luz, corriges la posición de todos los barcos y reduces la dispersión. La covarianza de la nube colapsa y sabes dónde está el barco con precisión quirúrgica.

---

## 🔬 2. Primeros Principios: Ecuaciones de Asimilación EnKF

1. **Estado del Ensamble**: Sea \( X = [x_1, x_2, \dots, x_N] \in \mathbb{R}^{d \times N} \) la matriz que contiene los \( N \) estados del ensamble.
2. **Matriz de Covarianza de Error**:
   \[
   P = \frac{1}{N-1} (X - \bar{x}) (X - \bar{x})^T
   \]
3. **Ganancia de Kalman Óptima**:
   \[
   K = P H^T (H P H^T + R)^{-1}
   \]
   Donde \( H \) es el operador de observación y \( R \) es la covarianza del ruido de medición.
4. **Paso de Actualización (Análisis)**:
   \[
   x_i^a = x_i^f + K (y_i - H x_i^f)
   \]

---

## 💻 3. Implementación Numérica en Python (NumPy Vectorizado)

```python
import numpy as np

class EnsembleKalmanFilterKata:
    def __init__(self, n_ensemble: int = 50, state_dim: int = 2, obs_noise: float = 0.05):
        self.N = n_ensemble
        self.d = state_dim
        self.R = np.eye(state_dim) * obs_noise
        self.H = np.eye(state_dim)
        # Inicializar ensamble alrededor del estado inicial
        self.ensemble = np.random.randn(self.d, self.N) + 20.0

    def predict(self, dt: float = 0.1, drift: float = 0.1):
        """Paso de pronóstico estocástico."""
        noise = np.random.normal(0, 0.02, size=(self.d, self.N))
        self.ensemble = self.ensemble + drift * dt + noise

    def update(self, observation: np.ndarray) -> float:
        """Paso de análisis EnKF y cálculo de covarianza."""
        # 1. Media y perturbaciones
        mean = np.mean(self.ensemble, axis=1, keepdims=True)
        A = self.ensemble - mean

        # 2. Covarianza muestral
        P = (A @ A.T) / (self.N - 1)

        # 3. Ganancia de Kalman
        S = self.H @ P @ self.H.T + self.R
        K = P @ self.H.T @ np.linalg.inv(S)

        # 4. Perturbar observaciones
        obs_perturbed = observation.reshape(-1, 1) + np.random.multivariate_normal(
            np.zeros(self.d), self.R, size=self.N
        ).T

        # 5. Actualizar ensamble
        innovation = obs_perturbed - self.H @ self.ensemble
        self.ensemble = self.ensemble + K @ innovation

        # Traza de covarianza posterior
        new_mean = np.mean(self.ensemble, axis=1, keepdims=True)
        new_A = self.ensemble - new_mean
        cov_trace = float(np.trace((new_A @ new_A.T) / (self.N - 1)))
        return cov_trace
```

---

## 🧪 4. Criterio de Graduación Consilium Romano

La prueba unitaria debe verificar que tras 10 iteraciones de asimilación con datos ruidosos, la traza de covarianza \( \text{Tr}(P) \) converja estrictamente por debajo de **0.50**.

---

## 🧠 5. Desafío Feynman y Rúbrica de Auto-Evaluación

> **Reto Feynman**: ¿Cómo le explicarías a un niño de 12 años cómo un barco pirata navega con una brújula rota y mapas borrosos usando un grupo de 50 marineros que tiran dados para calcular dónde están realmente?

### Rúbrica de Evaluación:
1. **Nivel 1 (Básico)**: Explica que cuando los instrumentos tienen ruido, simular muchas posibilidades y corregirlas con las medidas reales permite encontrar la posición más probable.
2. **Nivel 2 (Intermedio)**: Detalla los pasos del ciclo EnKF: propagación del estado, cálculo de la matriz de covarianza del error, cálculo de la ganancia de Kalman (\(K\)) y corrección por innovación ruidosa.
3. **Nivel 3 (Ph.D. / Staff)**: Formaliza la aproximación Monte Carlo de la distribución gaussiana condicional, la corrección de perturbación de observaciones de Burgers/Evensen y la reducción asintótica de la traza de covarianza hacia el límite de Cramér-Rao.

