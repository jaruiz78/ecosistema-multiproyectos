# Módulo 5.8: Entrenamiento In-Situ con BigQuery ML, Inferencia Edge Off-Heap y Asimilación EnKF

**Nivel de Rigor Académico**: Carnegie Mellon University (CMU 15-799) / MIT (6.884 Data-Driven Decision Systems) / Princeton (IAS).

---

## 1. Fundamentos Matemáticos y Teóricos

### A. La Filosofía "In-Situ": Cero Movimiento de Datos
En arquitecturas de Big Data tradicionales, el entrenamiento de modelos requiere extraer terabytes de datos desde el Data Warehouse hacia clústeres de cómputo externos (como Spark o GPU Pods). Esto introduce:
1. **Latencia de I/O de red**: \(O(N)\) proporcional al volumen de datos.
2. **Coste por duplicación**: Almacenamiento temporal en buckets de Cloud Storage y consumo de egress.

**BigQuery ML (BQML)** traslada el cómputo del modelo hacia los propios nodos de almacenamiento particionados, ejecutando gradiente descendente distribuido en paralelo sobre los bloques de disco nativos.

### B. Inferencia Edge Off-Heap con Complejidad \(O(1)\)
Para decisiones en tiempo de ejecución (microsegundos), el microservicio Java 25 no invoca APIs externas ni realiza asignaciones en el Heap. Se implementa un pool de buffers directos off-heap (`OffHeapTensorBufferPool`):

\[
T_{\text{inferencia}} = T_{\text{memoria nativa}} + T_{\text{cálculo matricial INT8}} \le 5\text{ ms}
\]

### C. Asimilación de Estado Continua mediante Filtro de Kalman Ensemble (EnKF)
El estado dinámico del ecosistema (tráfico, demanda de transfers, presión hidráulica, microredes eléctricas) se modela mediante un vector de estado \(x_t \in \mathbb{R}^n\). La actualización de covarianza en forma simétrica Joseph garantiza estabilidad numérica:

\[
P_t^a = (I - K_t H) P_t^f (I - K_t H)^T + K_t R K_t^T
\]

Donde \(K_t\) se resuelve sin inversión explícita resolviendo el sistema lineal \(S_t^T K_t^T = (P_t^f H^T)^T\).

---

## 2. Implementación en los Proyectos del Ecosistema

1. **`pctMultiMicroservices`**: Modelo `model_pct_hourly_demand_forecast` (ARIMA_PLUS) sobre `mv_task_hourly_kpis` para predecir la demanda turística en Panamá (`PA`) y República Dominicana (`DO`).
2. **`SaaSRegantes`**: Modelo `model_evapotranspiracion_prediccion` (Linear Regression) y simulador 1D Navier-Stokes de golpe de ariete (`water_hammer_pinn_simulator.py`).
3. **`AppViajes`**: Modelo `model_h3_surge_multiplier_predictor` sobre celdas hexagonales Uber H3 resolución 8.
4. **`core-kalman-twin`**: Inyección tensorial continua convergiendo con covarianza \(\text{trace}(P)/N < 0,5\) en menos de 10 ticks.

---

## 3. Preguntas de Autoevaluación y Certificación

1. *¿Por qué la deduplicación espacial en ventana deslizante en el BFF de Go reduce el tráfico a BigQuery sin perder precisión analítica?*
   * **Respuesta**: Porque descarta pings GPS redundantes emitidos dentro de la misma celda H3 (resolución 8, ~460m de radio) en intervalos menores a 5 segundos, donde la velocidad del vehículo no genera cambio de estado dimensional.
2. *¿Cómo protege la forma Joseph al filtro de Kalman en situaciones de shocks extremos?*
   * **Respuesta**: Al formular \(P^a = (I-K)P^f(I-K)^T + KRK^T\), se suman dos matrices semidefinidas positivas, garantizando que los errores de redondeo en punto flotante jamás generen varianzas negativas o matrices singulares.

---

## 4. Referencias y Enlaces a Código
* [`BqmlModelInSituManager.java`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter/corp-bigdata-ai-starter/src/main/java/com/corp/bigdata/BqmlModelInSituManager.java)
* [`OffHeapTensorBufferPool.java`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter/corp-bigdata-ai-starter/src/main/java/com/corp/bigdata/OffHeapTensorBufferPool.java)
* [`enkf_solver.py`](file:///home/jaruiz/Desarrollo/core/core-kalman-twin/src/core_kalman_twin/enkf_solver.py)
* [`ADR-010`](file:///home/jaruiz/Desarrollo/docs/adr/adr-010-bqml-edge-inference-and-kalman-twin-assimilation.md)
