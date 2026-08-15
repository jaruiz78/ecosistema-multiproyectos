# 🧪 Laboratorios Prácticos Feynman: Código Ejecutable "Zero-Setup"
## *Universidad Privada del Ecosistema & Cátedras de Ingeniería Práctica*

Este directorio contiene la suite de **Laboratorios Prácticos Interactivos y Autocontenidos** diseñados para comprobar empíricamente las leyes de la física computacional, la concurrencia y los algoritmos distribuidos con cero dependencias externas.

---

### 📂 Índice de Laboratorios Disponibles

| Lab | Archivo | Tecnología | Concepto Demostrado |
| :--- | :--- | :--- | :--- |
| **01** | [`01_lab_transformer_numpy_from_scratch.py`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/laboratorios_practicos/01_lab_transformer_numpy_from_scratch.py) | Python 3 + NumPy | Deconstrucción matemática completa de Multi-Head Attention (Vaswani 2017) sin PyTorch. |
| **02** | [`02_lab_raft_consensus_go_cluster.go`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/laboratorios_practicos/02_lab_raft_consensus_go_cluster.go) | Go 1.25 puro | Clúster Raft de 3 nodos con quórum mayoritario, reelección por timeout y tolerancia a fallos. |
| **03** | [`03_lab_false_sharing_cache_line_padding.go`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/laboratorios_practicos/03_lab_false_sharing_cache_line_padding.go) | Go 1.25 puro | Medición empírica de False Sharing en líneas de caché L1 (64 bytes) y aceleración de `~9.5x` con padding. |
| **04** | [`04_lab_kalman_filter_enkf_assimilation.py`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/laboratorios_practicos/04_lab_kalman_filter_enkf_assimilation.py) | Python 3 + NumPy | Asimilación estocástica de observaciones ruidosas con EnKF y convergencia de covarianza `< 0.50`. |
| **05** | [`05_lab_token_bucket_rate_limiter.go`](file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/laboratorios_practicos/05_lab_token_bucket_rate_limiter.go) | Go 1.25 puro | Algoritmo Token Bucket en \(\mathcal{O}(1)\) matemático sin hilos durmientes ni temporizadores de fondo. |

---

### 🚀 Cómo Ejecutar los Laboratorios

Todos los laboratorios se ejecutan de forma inmediata desde el terminal:
```bash
# 1. Ejecutar Transformer en NumPy Puro
python3 docs/formacion_ecosistema/laboratorios_practicos/01_lab_transformer_numpy_from_scratch.py

# 2. Ejecutar Clúster Raft en Go
go run docs/formacion_ecosistema/laboratorios_practicos/02_lab_raft_consensus_go_cluster.go

# 3. Ejecutar Benchmark de False Sharing
go run docs/formacion_ecosistema/laboratorios_practicos/03_lab_false_sharing_cache_line_padding.go

# 4. Ejecutar Asimilación EnKF
python3 docs/formacion_ecosistema/laboratorios_practicos/04_lab_kalman_filter_enkf_assimilation.py

# 5. Ejecutar Token Bucket Rate Limiter
go run docs/formacion_ecosistema/laboratorios_practicos/05_lab_token_bucket_rate_limiter.go
```
