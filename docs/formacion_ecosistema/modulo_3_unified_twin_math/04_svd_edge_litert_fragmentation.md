# Módulo 3 - Lección 4: Fragmentación Edge, SVD & Compresión para LiteRT

## 1. Descomposición en Valores Singulares (SVD)

Para trasladar modelos complejos entrenados en el backend a dispositivos móviles (Flutter/LiteRT) con **cero degradación térmica y consumo de batería mínimo**, aplicamos **SVD (Singular Value Decomposition)** sobre las matrices de pesos de las redes.

### Ecuación de SVD
Cualquier matriz de pesos $W \in \mathbb{R}^{m \times n}$ se descompone en:

$$W = U \cdot \Sigma \cdot V^T$$

donde $U$ es una matriz ortogonal $m \times m$, $\Sigma$ es una matriz diagonal de valores singulares $\sigma_1 \ge \sigma_2 \ge \dots \ge \sigma_r \ge 0$, y $V^T$ es la transpuesta de una matriz ortogonal $n \times n$.

### Truncamiento de Rango $k$ ($k \ll r$)
Aproximamos $W$ manteniendo únicamente los $k$ valores singulares más grandes:

$$W \approx W_k = U_k \cdot \Sigma_k \cdot V_k^T$$

Esto reduce el número de parámetros de $m \times n$ a **$k(m + n + 1)$**, logrando una compresión del 80-90% con pérdida de precisión insignificante (<1%).

---

## 2. Diagrama de Pipeline de Compresión LiteRT (Mermaid)

```mermaid
graph TD
    subgraph Backend / Cloud (Modelo Completo)
        FULL["Modelo Full Weight Matrix W (100MB)"]
    end

    subgraph Pipeline de SVD & Quantization
        SVD["SVD Truncado (Rangos k)"]
        QUANT["Cuantización FP32 -> INT8"]
    end

    subgraph Dispositivo Móvil / Edge (LiteRT)
        LITERT["Modelo Fragmentado LiteRT (.tflite / .bin < 5MB)"]
        EXEC["Inferencia Edge O(k(m+n)) - Cero Calentamiento"]
    end

    FULL --> SVD
    SVD --> QUANT
    QUANT --> LITERT
    LITERT --> EXEC
```

---

## 3. Script Python de Truncamiento SVD

```python
import numpy as np

def compress_weights_svd(weights: np.ndarray, rank_k: int) -> tuple[np.ndarray, np.ndarray]:
    """
    Comprime una matriz de pesos W (m x n) en dos factores L (m x k) y R (k x n) usando SVD.
    W_approx = L @ R
    """
    m, n = weights.shape
    U, s, Vt = np.linalg.svd(weights, full_matrices=False)

    # Retener únicamente los primeros k valores singulares
    U_k = U[:, :rank_k]
    s_k = s[:rank_k]
    Vt_k = Vt[:rank_k, :]

    # Factorización en dos capas densas reducidas
    L = U_k @ np.diag(np.sqrt(s_k))  # Matriz (m x k)
    R = np.diag(np.sqrt(s_k)) @ Vt_k  # Matriz (k x n)

    original_params = m * n
    compressed_params = (m * rank_k) + (rank_k * n)
    print(f"Compresión: {original_params} -> {compressed_params} parámetros ({compressed_params/original_params:.2%})")

    return L, R
```
