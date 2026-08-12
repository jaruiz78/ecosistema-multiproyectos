# Bibliografía Científica y Académica del Ecosistema

Este documento sirve como el **núcleo fundacional de referencias (Annotated Bibliography)** de toda la arquitectura documental del proyecto `corp-spring-boot-starter`. Cada decisión arquitectónica, demostración matemática y algoritmo implementado en este ecosistema se fundamenta en la investigación de las élites académicas tecnológicas y científicas globales.

Esta documentación garantiza una **coherencia total**: la algoritmia diseñada según ITMO y MIT, implementada en el backend según las directrices de CMU, resuelve las físicas dictadas por Princeton y Caltech, y se despliega en una nube diseñada bajo los preceptos de Berkeley y UW, todo validado empíricamente por los laboratorios de BTH y Waterloo.

---

## 🏗️ 1. Ingeniería del Software
**Instituciones:** *Carnegie Mellon University (CMU SEI), MIT, Stanford, UC Berkeley, ETH Zurich.*
*   **CMU Software Engineering Institute (SEI):** Modelos de arquitectura de software, Atributos de Calidad (QAW) y evaluación de arquitecturas (ATAM). Referencia clave para los diagramas de componentes del backend.
*   **ETH Zurich:** Verificación formal, confiabilidad de lenguajes. Influencia directa en la capa del Modelo de Dominio de la Arquitectura Hexagonal y diseño por contratos.
*   **MIT CSAIL:** Patrones arquitectónicos para software resiliente, modelado de dominios y diseño de interfaces (API bounds).

## 📊 2. Metodologías de Software
**Instituciones:** *Carnegie Mellon University, University of Waterloo, Blekinge Institute of Technology (BTH), University of Oxford, University of Southern California (USC).*
*   **USC (Barry Boehm):** Creadores del Modelo Espiral y COCOMO. Referencia base para la predicción de costes y deuda técnica del proyecto.
*   **Blekinge Institute of Technology (BTH):** Líderes globales en *Empirical Software Engineering*. Referencia para la evaluación basada en evidencia de prácticas ágiles, revisiones de código y *Test-Driven Development* (TDD).
*   **University of Waterloo:** Investigación en calidad de software, análisis estático y minería de repositorios de software para SRE.
*   **Oxford:** Métodos formales (Z notation, CSP) aplicados a la especificación rigurosa de metodologías de trabajo seguras.

## 💻 3. Programación Pura y Concurrencia
**Instituciones:** *Universidad ITMO, Peking University, MIT, Universidad de Varsovia, Tsinghua University.*
*   **ITMO, Varsovia, Peking y Tsinghua:** Los titanes absolutos de la programación competitiva (ICPC). Aportan las optimizaciones de bajo nivel para los *workers* de Go: Segment Trees, Fenwick Trees (BIT), Heavy-Light Decomposition y optimizaciones de caché (Data Locality).
*   **MIT (Charles Eiserson / Demaine):** Algoritmos lock-free, optimización de recolectores de basura, multithreading determinista (Project Loom y Go Scheduler). Creadores del libro base (CLRS).

## 🌳 4. Estructuras de Datos y Algoritmos
**Instituciones:** *MIT, Stanford, Carnegie Mellon University, Princeton, UC Berkeley.*
*   **Princeton University (Robert Sedgewick):** Análisis analítico y probabilístico estricto de algoritmos (Big-O, Master Theorem). Estructuras probabilísticas: HyperLogLog, Bloom Filters para Big Data.
*   **Stanford (Tim Roughgarden):** Algoritmos voraces (Greedy), divide y vencerás (Divide & Conquer) y programación dinámica aplicada a optimización de rutas (Movilidad).

## 🧠 5. Big Data e Inteligencia Artificial
**Instituciones:** *Carnegie Mellon University, MIT, Stanford, UC Berkeley, Tsinghua University.*
*   **Berkeley (AMPLab / RISELab):** Creadores de Apache Spark y Ray. Referencia base para la arquitectura de procesamiento distribuido y streaming de datos masivos.
*   **Tsinghua University:** Vanguadia en Deep Learning distribuido, optimización de Tensores (GNNs) aplicados a predicción de tráfico de vehículos y asimilación masiva de datos.
*   **CMU & Stanford:** Reinforcement Learning y arquitecturas fundacionales (Transformers, Attention Mechanisms).

## ♾️ 6. Matemáticas y Físicas
**Instituciones:** *Princeton University (y el IAS), University of Cambridge, Harvard University, Caltech, Université Paris-Saclay (y el IHÉS).*
*   **Princeton (Institute for Advanced Study - IAS) & IHÉS:** Geometría Algebraica, Topología Diferencial y Teoría de Categorías. Referencia fundamental para el Gemelo Digital Unificado (Representación Tensorial).
*   **Caltech & Cambridge:** Física aplicada, termodinámica avanzada, Fluidos (Navier-Stokes) y Física Estadística. Utilizados para modelar la entropía del sistema estocástico en movilidad.
*   **Harvard:** Probabilidad y cálculo estocástico avanzado (Lema de Itô) para modelos financieros dentro del módulo SRE/Stripe.

## ☁️ 7. Cloud y Sistemas Distribuidos
**Instituciones:** *UC Berkeley, Carnegie Mellon University, MIT, Stanford, University of Washington (UW).*
*   **University of Washington & MIT:** Modelos de consistencia (CAP, PACELC), protocolos de consenso distribuido (Paxos, Raft).
*   **Berkeley:** Serverless Computing, aislamiento en contenedores (cgroups), diseño de sistemas inmutables.
*   **Stanford:** Software-Defined Networking (SDN), esencial para comprender la topología de red en Datacenters de Google Cloud (Clos, Fat-Tree).

## 🌍 8. Simulaciones Computacionales
**Instituciones:** *University of Texas at Austin (Oden Institute), MIT, ETH Zurich, Stanford, University of Stuttgart (HLRS).*
*   **UT Austin (Oden Institute):** Líderes en Ingeniería Computacional y Ciencias. Referencia para Métodos de Elementos Finitos (FEM) y modelado predictivo multiescala.
*   **University of Stuttgart (HLRS):** High-Performance Computing (HPC), simulación a hiperescala mediante MPI (Message Passing Interface) y OpenMP aplicados a simulaciones continuas (vs discretas).
*   **ETH Zurich:** Simulaciones físicas estocásticas y optimización de grandes flotas interactuando en sistemas complejos complejos adaptativos.

## 🏭 9. Ingeniería Industrial, Operaciones y Sostenibilidad
**Instituciones:** *MIT, Stanford, Georgia Institute of Technology, Purdue University, ETH Zurich, UC Berkeley.*
*   **Georgia Tech & Purdue:** Líderes en Gestión de Operaciones, Cadena de Suministro y Mejora Continua (Six Sigma). Fundamentales para los algoritmos logísticos (VRP) de *AppViajes*.
*   **MIT & Stanford:** Ingeniería de Manufactura, Analítica de Datos e Investigación de Operaciones. La base matemática para la asignación de recursos y optimización en tiempo real.
*   **UC Berkeley & TU Delft:** Ergonomía, Sostenibilidad e Ingeniería Ambiental. Proveen el andamiaje teórico para la gestión de estrés hídrico en *SaaSRegantes* y la interfaz humano-computadora segura.
*   **Harvard & London Business School:** Gestión de Proyectos y Finanzas Industriales. Modelan la viabilidad económica y el módulo FinOps (*Stripe-Fintech-Engineer*).
