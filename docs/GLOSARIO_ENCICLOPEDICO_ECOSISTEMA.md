# GLOSARIO ENCICLOPÉDICO Y MATRIZ TERMINOLÓGICA DEL ECOSISTEMA
**Google Antigravity Sovereign Framework** | **Nivel de Rigor:** CMU / MIT / Stanford / Berkeley Benchmark

Este documento establece las definiciones formales, formulaciones matemáticas y garantías arquitectónicas de todos los conceptos cardinales utilizados en el ecosistema de software y simulaciones.

---

## 1. Concurrencia, Runtime y Arquitectura de Sistemas

### Ahead-of-Time Compilation (AOT)
* **Definición:** Técnica de traducción de código fuente o bytecode a código máquina nativo previa al tiempo de ejecución, eliminando la sobrecarga de interpretación y optimización JIT (Just-In-Time) durante el arranque.
* **Aplicación en el Ecosistema:** Compilación GraalVM Native Image y Project Leyden en Java 25 para alcanzar arranques en frío (*cold-start*) `<80ms` en Google Cloud Run.

### Carrier Thread Pinning
* **Definición:** Fenómeno degradante en Java Virtual Threads (Project Loom) donde un hilo virtual queda anclado a su hilo portador del sistema operativo (*Carrier Thread*), bloqueándolo e impidiendo que el scheduler M:N desmonte la tarea durante una operación de E/S bloqueante.
* **Causa & Solución:** Ocurre al usar bloques `synchronized` o llamadas nativas JNI. Se resuelve sustituyendo obligatoriamente `synchronized` por `java.util.concurrent.locks.ReentrantLock` o estructuras lock-free (`VarHandle`).

### Class Data Sharing (CDS) & Project Leyden
* **Definición:** Mecanismo de la JVM HotSpot que precarga y preprocesa metadatos de clases en un archivo de memoria compartida mapeada (`.jsa`), permitiendo que múltiples instancias JVM arranquen casi instantáneamente sin repetir la fase de carga y verificación de bytecode.
* **Pipeline:** Entrenamiento automatizado mediante `scripts/bin/leyden_cds_trainer.sh`.

### Communicating Sequential Processes (CSP)
* **Definición:** Modelo formal de concurrencia formulado por C.A.R. Hoare (1978) donde los procesos concurrentes interactúan exclusivamente mediante el paso de mensajes a través de canales síncronos o asíncronos, en lugar de compartir memoria con bloqueos explícitos.
* **Aplicación:** Base del runtime de Go y sus canales (`chan`), utilizado en los workers de red y procesadores de telemetría.

### Escape Analysis
* **Definición:** Análisis estático realizado por los compiladores de Go y Java para determinar si la vida útil de una variable excede el ámbito de su función invocadora. Si no "escapa", la variable se asigna en el *stack* (coste cero de recolección de basura) en lugar del *heap*.

---

## 2. Gemelo Digital, Física Tensorial y Asimilación Estocástica

### Ensemble Kalman Filter (EnKF)
* **Definición:** Algoritmo de asimilación secuencial estocástica de datos que aproxima la distribución de probabilidad del estado de un sistema dinámico mediante un conjunto de trayectorias muestrales (*ensamble*).
* **Ecuación de Actualización de Ganancia:**
  \[
  K = P^f H^T (H P^f H^T + R)^{-1}
  \]
  Donde \(P^f\) es la matriz de covarianza del ensamble pronosticado, \(H\) es el operador de observación y \(R\) es la covarianza del error de medición.
* **Criterio de Convergencia del Ecosistema:** La traza normalizada de la matriz de covarianza debe converger por debajo de \(0.5\) en menos de 10 ticks en `simulations_telemetry.db`.

### Physics-Informed Neural Networks (PINNs)
* **Definición:** Redes neuronales profundas entrenadas para resolver ecuaciones diferenciales parciales (PDEs) integrando directamente las leyes físicas (conservación de masa, momento y energía) dentro de la función de pérdida del optimizador:
  \[
  \mathcal{L} = \mathcal{L}_{\text{datos}} + \lambda \mathcal{L}_{\text{PDE}}
  \]
* **Uso en el Ecosistema:** Modelado de fluidos incompresibles (Navier-Stokes) y prevención de transitorios hidráulicos (*Water Hammer* / Golpe de Ariete) en [`ProyectoAgua`](file:///home/jaruiz/Desarrollo/apps/ProyectoAgua) y [`SaaSRegantes`](file:///home/jaruiz/Desarrollo/SaaSRegantes).

### Projected Entangled Pair States (PEPS)
* **Definición:** Redes tensoriales bidimensionales utilizadas para representar estados cuánticos y sistemas complejos multidominio con interacciones locales, permitiendo contracciones aproximadas en orden asintótico \(O(N)\).
* **Aplicación:** Núcleo de acoplamiento de variables en `tensor_gnn_core.py` (cruzando energía, agua, movilidad y economía).

### Singular Value Decomposition (SVD) & LiteRT
* **Definición:** Factorización matricial de la forma \(A = U \Sigma V^T\) que permite truncar valores singulares de menor energía para comprimir tensores de inferencia sin pérdida apreciable de fidelidad predictiva, habilitando su ejecución local en dispositivos móviles mediante Google LiteRT a coste `$0.00 USD/mes`.

---

## 3. Indexación Espacial, Movilidad y Algoritmia

### Uber H3 (Discrete Global Grid System)
* **Definición:** Sistema de indexación espacial hexagonal jerárquico global desarrollado por Uber. Proporciona celdas poligonales de área cuasi-uniforme con distancias euclidianas idénticas a todos sus 6 vecinos inmediatos, eliminando las distorsiones métricas de las cuadrículas ortogonales (latitud/longitud).
* **Resoluciones Estándar del Ecosistema:**
  * **H3-7:** Macro-zonas urbanas (\(\approx 5.16 \text{ km}^2\)) para balance macroeconómico y asignación energética.
  * **H3-8:** Densidad de demanda y tarifas dinámicas (*Surge Pricing*) en [`AppViajes`](file:///home/jaruiz/Desarrollo/AppViajes) (\(\approx 0.73 \text{ km}^2\)).
  * **H3-9:** Parcelas agrícolas de regadío y paradas de última milla (\(\approx 0.10 \text{ km}^2\)).

### Contraction Hierarchies (OSRM)
* **Definición:** Algoritmo de aceleración de caminos mínimos sobre grafos viales que precalcula atajos jerárquicos basados en la importancia de los nodos, permitiendo consultas de distancias y tiempos de viaje en sub-milisegundos (\(<1\text{ms}\)).

### Discrete Event Simulation (DES) vs. Agent-Based Modeling (ABM)
* **DES:** Simulación basada en una cola de eventos cronológicos discretos (llegadas Poisson a aeropuertos, turnos de riego).
* **ABM:** Simulación donde entidades autónomas (conductores, agricultores, robots) toman decisiones descentralizadas basadas en funciones de utilidad y estados internos.

---

## 4. Cloud-Native, Bases de Datos y FinOps

### Row-Level Security (RLS) & Multi-Tenancy Celular
* **Definición:** Modelo de seguridad donde las políticas de acceso a los datos se evalúan a nivel de fila o documento individual en la base de datos (Firestore/Cloud SQL), asegurando que un `tenant_id` nunca pueda leer ni modificar información de otro inquilino bajo ningún vector de ataque.

### Almacenamiento Columnar Capacitor (BigQuery)
* **Definición:** Formato de almacenamiento columnar propietario de Google BigQuery que organiza los datos en bloques comprimidos por columnas y vectores SIMD. Exige particionamiento por fecha y clustering por `tenant_id` para garantizar consultas con coste unitario \(<0.015\text{ USD/MAU/mes}\).

### Sagas Pattern & Transactional Outbox
* **Definición:** Patrón de diseño para transacciones distribuidas en microservicios donde cada servicio ejecuta una transacción local y emite un evento a una tabla "Outbox". Si un paso falla, se disparan transacciones compensatorias en orden inverso.

---

## 5. DevSecOps, Integridad y SRE

### SLSA L3/L4 (Supply-chain Levels for Software Artifacts)
* **Definición:** Marco de seguridad estándar que garantiza que los artefactos binarios fueron compilados en plataformas de CI/CD aisladas e inmutables, acompañados de un SBOM (Software Bill of Materials) firmado criptográficamente mediante Sigstore/Cosign.

### OpenTelemetry (OTel) RED & USE Metrics
* **RED Method:** Rate (peticiones/seg), Errors (fallos/seg), Duration (latencia en percentiles p95/p99) para servicios orientados a peticiones.
* **USE Method:** Utilization (%), Saturation (longitud de colas), Errors (recuento) para recursos de infraestructura (CPU, memoria, descriptores de fichero).
