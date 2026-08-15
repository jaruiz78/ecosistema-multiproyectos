# Módulo 3 - Lección 2: Asimilación de Datos con Filtro de Kalman Ensembled (EnKF)

---

## 1. 🐣 Ancla Mental Feynman & Analogía Isomórfica

### El Modelo Intuitivo: Asimilación de Datos con Filtro de Kalman Ensembled (EnKF)
Para comprender **Asimilación de Datos con Filtro de Kalman Ensembled (EnKF)** sin caer en la trampa de la jerga técnica, debemos anclar el concepto en un problema físico observable:
* Todo sistema en computación resuelve un dilema fundamental: cómo organizar recursos limitados (tiempo de cálculo, espacio de memoria, ancho de banda o energía) para que el trabajo se realice sin bloqueos ni errores.
* En **Asimilación de Datos con Filtro de Kalman Ensembled (EnKF)**, la clave reside en eliminar pasos redundantes y asegurar que cada componente conozca únicamente la información mínima indispensable para cumplir su función, tal como una línea de montaje bien coordinada donde nadie tiene que adivinar qué hizo el compañero anterior.

---


## 1. Fundamentos Teóricos del Filtro EnKF

El **Ensemble Kalman Filter (EnKF)** es un método de asimilación de datos estocásticos para estimar los estados de sistemas no lineales de alta dimensión. Mantiene un ensamble de $N_e$ miembros de estado para aproximar la matriz de covarianza de error del sistema.

### Ecuaciones de Actualización de EnKF

1. **Pronóstico del Ensamble**:
   $$x_i^f(k) = f(x_i^a(k-1)) + w_i(k), \quad i = 1, \dots, N_e$$

2. **Matriz de Ganancia de Kalman ($K$)**:
   $$K = P^f H^T \left( H P^f H^T + R \right)^{-1}$$

3. **Actualización de Estado (Asimilación)**:
   $$x_i^a(k) = x_i^f(k) + K \left( y_i(k) - H x_i^f(k) \right)$$

donde $P^f$ es la covarianza calculada a partir de los miembros del ensamble, $y_i(k)$ son las observaciones perturbadas con ruido $R$, y $H$ es el operador de observación.

---

## 2. Diagrama del Bucle de Asimilación (Mermaid)

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

## 3. Implementación Vectorizada con NumPy

```python
import numpy as np

class EnsembleKalmanFilter:
    def __init__(self, num_states: int, num_obs: int, ensemble_size: int = 50):
        self.n = num_states
        self.m = num_obs
        self.Ne = ensemble_size
        # Inicialización del ensamble de estados [n_states x Ne]
        self.X = np.random.randn(self.n, self.Ne) * 0.1

    def predict(self, transition_func):
        """Aplica la función de transición de estado a cada miembro del ensamble"""
        for i in range(self.Ne):
            noise = np.random.normal(0, 0.01, size=self.n)
            self.X[:, i] = transition_func(self.X[:, i]) + noise

    def update(self, observations: np.ndarray, H: np.ndarray, R: np.ndarray):
        """Asimilación de observaciones reales perturbadas"""
        # 1. Media y anomalías del ensamble
        x_mean = np.mean(self.X, axis=1, keepdims=True)
        A = self.X - x_mean

        # 2. Observaciones perturbadas
        Y = np.zeros((self.m, self.Ne))
        for i in range(self.Ne):
            Y[:, i] = observations + np.random.multivariate_normal(np.zeros(self.m), R)

        # 3. Matriz de innovación y Ganancia de Kalman
        HA = H @ A
        pf_ht = (A @ HA.T) / (self.Ne - 1)
        h_pf_ht_r = (HA @ HA.T) / (self.Ne - 1) + R
        K = pf_ht @ np.linalg.inv(h_pf_ht_r)

        # 4. Actualización del ensamble
        Hx = H @ self.X
        self.X = self.X + K @ (Y - Hx)

    def check_convergence(() -> float:
        """Calcula la traza de la covarianza de error"""
        x_mean = np.mean(self.X, axis=1, keepdims=True)
        A = self.X - x_mean
        cov = (A @ A.T) / (self.Ne - 1)
        return float(np.trace(cov))
```

---

## 4. Criterio de Aprobación en el Gemelo Digital

Si la covarianza de error calculada vía `check_convergence()` no desciende por debajo de **0.5 tras 10 Ticks de simulación**, la teoría o heurística propuesta es rechazada inmediatamente por el orquestador.


---

## 5. 🎯 Desafío Feynman & Auto-Evaluación sin Jerga

> [!NOTE]
> **El Reto de los 12 Años**: Explica el mecanismo esencial y la utilidad práctica de **Asimilación de Datos con Filtro de Kalman Ensembled (EnKF)** a un estudiante de secundaria, **sin usar las palabras:** "Asimilación", "de", "Datos" ni tecnicismos complejos de memoria.

### Criterio de Verificación
* **Aprobado**: Si logras construir una analogía mecánica o física del mundo real donde se entienda por qué fallaría el sistema sin esta solución y cómo resuelve el problema en términos elementales.
* **No Aprobado**: Si dependes de definiciones de diccionario, siglas de frameworks o nombres de patrones sin explicar la causa física subyacente.



---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
