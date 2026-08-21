# core-kalman-twin — Motor de Asimilación Estocástica de Datos (Ensemble Kalman Filter)

Módulo algorítmico híbrido (**Python 3.13+ / 3.14 [Free-Threaded / No-GIL] / NumPy / SciPy** y puente **Java 25 LTS**) para la asimilación secuencial de datos estocásticos y estimación óptima del estado del Gemelo Digital Unificado (Unified Twin PEPS).

---

## 1. Fundamentos Teóricos y Formulación Matemática

Basado en la formulación de **Evensen (2003)** y el esquema de análisis de **Burgers et al. (1998)**, el EnKF resuelve el problema de estimación bayesiana no lineal mediante un ensamble de \(N\) realizaciones de estado estocástico (\(E \in \mathbb{R}^{n \times N}\)).

### Ecuaciones Gobernantes

1. **Propagación del Ensamble (Paso de Predicción):**
   - Para modelos dinámicos lineales:
     \[ E_{k|k-1} = F \cdot E_{k-1} + W, \quad W \sim \mathcal{N}(0, Q) \]
   - Para operadores dinámicos no lineales arbitrarios:
     \[ E_{k|k-1} = f(E_{k-1}) + W \]

2. **Matriz de Anomalías / Perturbaciones de Estado:**
   \[ \bar{E} = \frac{1}{N} E \mathbf{1} \mathbf{1}^T, \quad A = E - \bar{E} \]

3. **Asimilación Observacional con Perturbación de Datos (Burgers et al. 1998):**
   Dado un vector de observaciones \(z \in \mathbb{R}^m\) y una matriz de covarianza de ruido \(R \in \mathbb{R}^{m \times m}\):
   \[ Z = z \mathbf{1}^T + V, \quad V \sim \mathcal{N}(0, R) \]

4. **Matriz de Covarianza de Innovación y Regularización de Tikhonov:**
   \[ S = \frac{1}{N-1} (H A)(H A)^T + R + \epsilon I \]
   donde \(\epsilon \cdot I\) (\(\epsilon = 10^{-7}\)) actúa como regularizador de Tikhonov frente a matrices de covarianza de innovación mal condicionadas.

5. **Resolución Numéricamente Estable de la Ganancia de Kalman:**
   En lugar de la inversión explícita directa \(S^{-1}\), se aplica **Factorización de Cholesky**:
   \[ S = L L^T \implies K S = \frac{1}{N-1} A (H A)^T \implies K = \text{cho\_solve}(L, \text{RHS}) \]

6. **Actualización de los Miembros del Ensamble:**
   \[ E_{k|k} = E_{k|k-1} + K (Z - H E_{k|k-1}) \]

7. **Traza de Covarianza y Criterio de Convergencia Six Sigma:**
   \[ P = \frac{1}{N-1} A A^T, \quad \operatorname{Tr}(P) = \sum_{i=1}^n \sigma_i^2 < 0.20 \]

---

## 2. Componentes e Integración

- **`src/enkf_solver.py`**:
  - `EnsembleKalmanFilter`: Clase principal vectorizada con NumPy y tipado `numpy.typing.NDArray`.
  - `predict(F, Q)`: Avance lineal en \(O(n \cdot N)\).
  - `predict_nonlinear(f, Q)`: Avance no lineal con función vectorizada.
  - `update(z, H, R, tikhonov_epsilon)`: Asimilación por Cholesky con fallback a pseudoinversa de Moore-Penrose.
  - `get_state_mean()` y `get_covariance_trace()`: Métricas de estado y convergencia.
- **Integración con Gemelo Digital:**
  - Invocado directamente por `scripts/simulations/tensor_gnn_core.py` para asimilar la telemetría en tiempo real de los 8 clusters industriales del ecosistema.

---

## 3. Pruebas y Certificación

- **Testing Hermético:**
  ```bash
  pytest tests/
  mvn clean test
  ```