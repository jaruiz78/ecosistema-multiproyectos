# Módulo 3 - Lección 8: Fragmentación Edge, SVD & Compresión para LiteRT

---

## 1. 🐣 Rincón Junior: Conceptos desde Cero & Analogías

### ¿Qué es la Descomposición SVD y la Fragmentación Edge?
Imagina enviar una fotografía HD de 50 Megabytes por WhatsApp a un móvil con poca cobertura. Si la envías completa, tardará minutos y agotará la batería. Si comprimes la imagen eliminando detalles imperceptibles, se envía en 1 segundo y se ve prácticamente igual.

La **Descomposición en Valores Singulares (SVD)** es la fórmula matemática que comprime las matrices de pesos de las redes neuronales del backend para que puedan ejecutarse en teléfonos móviles (**LiteRT / TensorFlow Lite**) en < 5MB de espacio sin calentar el terminal ni consumir batería.

---

## 2. 📐 Arquitectura Visual & Diagrama Mermaid

```mermaid
graph TD
    subgraph Backend Cloud (Modelo Pesado)
        FULL["Matriz Original W (100 MB)"]
    end

    subgraph Pipeline SVD & LiteRT
        SVD["SVD Truncado (Rango k)"]
        QUANT["Cuantización FP32 -> INT8"]
    end

    subgraph Móvil / Edge Execution
        LITERT["Modelo LiteRT Comprimido (< 5 MB)"]
        EXEC["Inferencia Edge O(k(m+n)) - Zero Calentamiento"]
    end

    FULL --> SVD
    SVD --> QUANT
    QUANT --> LITERT
    LITERT --> EXEC
```

---

## 3. 🚀 Guía Paso a Paso e Implementación Práctica (0 a 100)

```python
import numpy as np

def compress_weights_svd(weights: np.ndarray, rank_k: int) -> tuple[np.ndarray, np.ndarray]:
    """
    Comprime una matriz de pesos W (m x n) en dos factores L (m x k) y R (k x n) usando SVD.
    W_approx = L @ R
    """
    m, n = weights.shape
    U, s, Vt = np.linalg.svd(weights, full_matrices=False)

    U_k = U[:, :rank_k]
    s_k = s[:rank_k]
    Vt_k = Vt[:rank_k, :]

    L = U_k @ np.diag(np.sqrt(s_k))  # Matriz reducida L
    R = np.diag(np.sqrt(s_k)) @ Vt_k  # Matriz reducida R

    print(f"Parámetros: {m*n} -> {(m*rank_k) + (rank_k*n)}")
    return L, R
```

---

## 4. 🧠 Referencia Senior: Cheatsheet, Performance & Internals

### Formulación Matemática de SVD Truncado

$$W \approx W_k = U_k \cdot \Sigma_k \cdot V_k^T, \quad \text{Compresión: } \frac{k(m+n+1)}{m \cdot n}$$

---

## 5. ⚠️ Errores Comunes & Anti-Patrones (Gotchas)

1. **Truncar un rango $k$ demasiado agresivo ($k < 5$) en capas de salida críticas**:
   * *Síntoma*: Caída drástica de la precisión del modelo en más de un 15%.
   * *Solución*: Mide siempre el error relativo $\|W - W_k\|_F / \|W\|_F$ y mantén una pérdida $< 1\%$.
