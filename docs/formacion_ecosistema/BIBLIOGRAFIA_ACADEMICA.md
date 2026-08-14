# Bibliografía Científica y Académica del Ecosistema

Este documento sirve como el **núcleo fundacional de referencias (Annotated Bibliography)** de toda la arquitectura documental del proyecto `corp-spring-boot-starter`, `tensor_gnn_core.py`, `AppViajes`, `SaaSRegantes` y los 19 verticales en `apps/`. Cada decisión arquitectónica, demostración matemática y algoritmo implementado en este ecosistema se fundamenta en la investigación de las élites académicas tecnológicas y científicas globales.

---

## 🏛️ 1. Papers Seminales y Referencias Primarias de Élite

### A. Concurrencia, Sistemas Operativos y Runtimes
1. **Dijkstra, E. W. (1965).** *"Solution of a problem in concurrent programming control"*. Communications of the ACM, 8(9), 569.
   * *Aplicación:* Exclusión mutua formal y estructuras lock-free en Java 25 (`VarHandle`).
2. **Hoare, C. A. R. (1978).** *"Communicating sequential processes"*. Communications of the ACM, 21(8), 666-677.
   * *Aplicación:* Modelo de paso de mensajes y canales concurrentes en el runtime de Go.
3. **Pugh, W. (2000).** *"The Java memory model"*. ACM SIGPLAN Notices, 35(3), 197-206.
   * *Aplicación:* Garantías *happens-before*, visibilidad en caché L1/L2/L3 y optimizaciones en HotSpot.
4. **Shipilëv, A. (2014-2024).** *"JVM Anatomy Quarks"*. Red Hat & OpenJDK.
   * *Aplicación:* Eliminación del *False Sharing*, padding de caché y concurrencia sin bloqueo.

### B. Sistemas Distribuidos, Consenso y Almacenamiento
1. **Lamport, L. (1978).** *"Time, clocks, and the ordering of events in a distributed system"*. Communications of the ACM, 21(7), 558-565.
   * *Aplicación:* Relojes lógicos y causalidad en eventos de despacho y ledgers inmutables.
2. **Ongaro, D., & Ousterhout, J. (2014).** *"In search of an understandable consensus algorithm (Raft)"*. USENIX ATC 2014 (Stanford University).
   * *Aplicación:* Control plane de Kubernetes y consenso en `core-govtech-ledger`.
3. **Corbett, J. C. et al. (Google Research, 2013).** *"Spanner: Google’s globally distributed database"*. ACM TOCS, 31(3), 1-22.
   * *Aplicación:* Algoritmo TrueTime, relojes con incertidumbre acotada y transacciones ACID distribuidas.
4. **Armbrust, M. et al. (UC Berkeley RISELab, 2010).** *"A view of cloud computing"*. Communications of the ACM, 53(4), 50-58.
   * *Aplicación:* Elasticidad serverless y modelo de auto-scaling en Google Cloud Run.

### C. Física Tensorial, Asimilación Estocástica y Gemelo Digital
1. **Evensen, G. (1994, 2003).** *"Sequential data assimilation with a nonlinear quasi-geostrophic model using Monte Carlo methods to forecast error statistics (EnKF)"*. Journal of Geophysical Research.
   * *Aplicación:* Motor maestro de asimilación estocástica en `tensor_gnn_core.py` y [`core-kalman-twin`](file:///home/jaruiz/Desarrollo/core/core-kalman-twin).
2. **Raissi, M., Perdikaris, P., & Karniadakis, G. E. (2019).** *"Physics-informed neural networks: A deep learning framework for solving forward and inverse problems involving nonlinear partial differential equations"*. Journal of Computational Physics, 378, 686-707.
   * *Aplicación:* Resolución de fluidos incompresibles (Navier-Stokes) y golpe de ariete (*Water Hammer*) en redes presurizadas de regadío.
3. **Verstraete, F., Murg, V., & Cirac, J. I. (2008).** *"Matrix product states, projected entangled pair states, and variational renormalization group methods for quantum spin systems"*. Advances in Physics, 57(2), 143-224.
   * *Aplicación:* Representación y contracción tensorial de interacciones multidominio en $O(N)$ en el gemelo digital.
4. **Deb, K., Pratap, A., Agarwal, S., & Meyarivan, T. (2002).** *"A fast and elitist multiobjective genetic algorithm: NSGA-II"*. IEEE Transactions on Evolutionary Computation, 6(2), 182-197.
   * *Aplicación:* Optimización multiobjetivo (coste vs latencia vs emisiones) en logística y despacho energético.

### D. Indexación Espacial y Enrutamiento
1. **Brodsky, I. (2018).** *"H3: Uber’s Hexagonal Hierarchical Spatial Index"*. Uber Engineering Whitepaper.
   * *Aplicación:* Indexación espacial hexagonal jerárquica en resoluciones H3-7 a H3-9 para cálculo de densidad y precios dinámicos.
2. **Geisberger, R., Sanders, P., Schultes, D., & Delling, D. (2008).** *"Contraction Hierarchies: Faster and Simpler Hierarchical Routing in Road Networks"*. WEA 2008.
   * *Aplicación:* Ruteo de sub-milisegundo en `OSRM` para despacho multimodal en [`AppViajes`](file:///home/jaruiz/Desarrollo/AppViajes).

### E. Site Reliability Engineering y Calidad
1. **Beyer, B., Jones, C., Petoff, J., & Murphy, N. R. (2016).** *"Site Reliability Engineering: How Google Runs Production Systems"*. O'Reilly Media.
   * *Aplicación:* Definición matemática de SLIs/SLOs, Error Budgets y cultura Blameless Postmortem.
2. **Nygard, M. T. (2018).** *"Release It!: Design and Deploy Production-Ready Software"*. Pragmatic Bookshelf.
   * *Aplicación:* Circuit Breakers, Bulkheads y mitigación de fallos en cascada en microservicios.

---

## 🏛️ 2. Mapeo Institucional de Excelencia

| Dominio Técnico | Universidades / Instituciones Líderes | Concepto Clave Integrado en el Ecosistema |
| :--- | :--- | :--- |
| **Ingeniería del Software** | Carnegie Mellon (CMU SEI), MIT, ETH Zurich | Arquitectura Hexagonal, Atributos de Calidad QAW y DDD Puro. |
| **Programación Competitiva** | Universidad ITMO, Varsovia, Tsinghua, Peking | Segment Trees, optimización de caché y algoritmia $O(1)$. |
| **Concurrencia & Memoria** | MIT CSAIL, Oracle Labs, Blekinge (BTH) | Virtual Threads Loom, Leyden CDS y Zero-Mockito TDD. |
| **Matemáticas & Físicas** | Princeton (IAS), Cambridge, Caltech, Paris-Saclay | Redes Tensoriales PEPS, Asimilación EnKF y PDEs Navier-Stokes. |
| **Big Data & Cloud** | UC Berkeley (AMPLab/RISELab), Google Research | Serverless Cloud Run, BigQuery Capacitor y Spanner TrueTime. |
| **SRE & Operaciones** | Stanford, Georgia Tech, Purdue University | Ergonomía segura, SRE Error Budgets y Six Sigma Quality. |
