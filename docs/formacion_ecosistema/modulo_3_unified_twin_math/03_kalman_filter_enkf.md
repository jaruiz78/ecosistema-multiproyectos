# Módulo 3 - Lección 3: Asimilación de Datos con Filtro de Kalman Ensembled (EnKF)

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es el Filtro de Kalman y por qué usamos EnKF?
Imagina que conduces un coche en un túnel oscuro sin GPS. Conoces tu velocidad actual y puedes estimar dónde estás (**Pronóstico**). Al salir del túnel, echas un vistazo rápido a una señal de tráfico (**Observación real**). 

El **Filtro de Kalman** es la fórmula matemática que combina de forma óptima tu estimación imprecisa con la observación ruidosa para calcular tu posición exacta. El **Ensemble Kalman Filter (EnKF)** hace esto manteniendo un "grupo" (ensamble) de 50 o 100 simulaciones paralelas para aproximar la incertidumbre en sistemas complejos no lineales.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Ensamble de Estados (Ne miembros)
        E_PRED["Pronóstico de Ensamble X_f"]
    end

    subgraph Datos de Telemetría Real
        OBS["Observaciones y(k) (Sensores / APIs)"]
    end

    E_PRED --> COV["Cálculo de Covarianza Pf"]
    OBS --> NOISE["Adición de Ruido Perturbado R"]
    COV --> K_GAIN["Matriz Ganancia Kalman K"]
    NOISE --> K_GAIN

    K_GAIN --> UPDATE["Actualización de Ensamble X_a"]
    UPDATE --> CONV_CHECK{"¿Covarianza < 0.5 en 10 Ticks?"}
    
    CONV_CHECK -->|Sí| ACCEPT["Aprobar Modelo Estocástico"]
    CONV_CHECK -->|No| REJECT["Rechazar Teoría Matemática"]
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```python
import numpy as np

class EnsembleKalmanFilter:
    def __init__(self, num_states: int, num_obs: int, ensemble_size: int = 50):
        self.n = num_states
        self.m = num_obs
        self.Ne = ensemble_size
        self.X = np.random.randn(self.n, self.Ne) * 0.1

    def predict(self, transition_func):
        for i in range(self.Ne):
            noise = np.random.normal(0, 0.01, size=self.n)
            self.X[:, i] = transition_func(self.X[:, i]) + noise

    def update(self, observations: np.ndarray, H: np.ndarray, R: np.ndarray):
        x_mean = np.mean(self.X, axis=1, keepdims=True)
        A = self.X - x_mean

        Y = np.zeros((self.m, self.Ne))
        for i in range(self.Ne):
            Y[:, i] = observations + np.random.multivariate_normal(np.zeros(self.m), R)

        HA = H @ A
        pf_ht = (A @ HA.T) / (self.Ne - 1)
        h_pf_ht_r = (HA @ HA.T) / (self.Ne - 1) + R
        K = pf_ht @ np.linalg.inv(h_pf_ht_r)

        Hx = H @ self.X
        self.X = self.X + K @ (Y - Hx)

    def get_error_covariance_trace(self) -> float:
        x_mean = np.mean(self.X, axis=1, keepdims=True)
        A = self.X - x_mean
        cov = (A @ A.T) / (self.Ne - 1)
        return float(np.trace(cov))
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Complejidad del Algoritmo EnKF

| Fase EnKF | Ecuación Principal | Complejidad Big-O |
| :--- | :--- | :--- |
| **Pronóstico** | $x_i^f = f(x_i^a) + w_i$ | $O(N_e \cdot \text{Coste}(f))$ |
| **Ganancia de Kalman ($K$)** | $K = P^f H^T (H P^f H^T + R)^{-1}$ | $O(m^3 + m \cdot n \cdot N_e)$ (Inversión de matriz $m \times m$) |

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Inestabilidad por tamaño de ensamble escaso ($N_e < 10$)**:
   * *Síntoma*: Divergencia en la actualización del estado y colapso de la covarianza.
   * *Solución*: Utiliza un tamaño de ensamble mínimo de $N_e \ge 50$ miembros.
