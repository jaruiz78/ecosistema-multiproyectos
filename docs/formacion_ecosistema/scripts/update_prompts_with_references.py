#!/usr/bin/env python3
import os
import sys
import json
import time
import urllib.request

NOTION_TOKEN = os.getenv("NOTION_TOKEN", "")
NOTION_VERSION = "2022-06-28"
STARTER_COMUN_PAGE_ID = "379eb64b-620b-8131-9016-d9388b1b6c12"
PROMPTS_PAGE_ID = "3b9eb64b-620b-810c-a969-d5c3b86aa9c7"

PROMPTS_FILE = "/home/jaruiz/Desarrollo/corp-spring-boot-starter/docs/formacion_ecosistema/PROMPTS_NOTION_AI_CUADERNOS.md"

content = """# Prompts de Notion AI para la Generación de Cuadernos Formativos (Notebooks)
## Proyecto: Starter Común — Ecosistema Corporativo de Alto Rendimiento

> [!IMPORTANT]
> **Instrucciones de Contexto Acotado en Notion AI:**
> Para evitar alucinaciones y garantizar que Notion AI **SOLO utilice los documentos de referencia exclusivos** de cada área (incluso si toda la base de datos está disponible en Notion):
> 1. En la casilla de consulta de Notion AI, utiliza la mención `@` para seleccionar únicamente los documentos indicados en el campo **`📄 Documentos de Referencia Exclusivos`**.
> 2. Copia y pega el bloque del Prompt.

---

## 📚 SECCIÓN A: LENGUAJES DE PROGRAMACIÓN (ÁREAS INDEPENDIENTES)

---

### ☕ Prompt 1: Cuaderno de Estudio del Lenguaje Java (Java 25 LTS & Spring Boot 4.0)

**📄 Documentos de Referencia Exclusivos (Módulo 1):**
- `@ [M1: Java & Spring] 01 Java 25 Sintaxis Y Tipos Avanzado`
- `@ [M1: Java & Spring] 01 Java25 Fundamentos Modernos`
- `@ [M1: Java & Spring] 01 Java25 Virtual Threads Loom`
- `@ [M1: Java & Spring] 02 Jvm Arquitectura Interna Bytecode`
- `@ [M1: Java & Spring] 02 Project Leyden Cds Aot`
- `@ [M1: Java & Spring] 03 Modelo De Memoria Java Jmm`
- `@ [M1: Java & Spring] 03 Spring Boot4 Fundamentos Arquitectura`
- `@ [M1: Java & Spring] 04 Garbage Collection Internals`
- `@ [M1: Java & Spring] 04 Project Leyden Cds Aot`
- `@ [M1: Java & Spring] 05 Jit Compilation C1 C2 Graal`
- `@ [M1: Java & Spring] 05 Opentelemetry Otel Observabilidad`
- `@ [M1: Java & Spring] 06 Virtual Threads Loom Internals`
- `@ [M1: Java & Spring] 07 Programacion Lock Free Varhandles`
- `@ [M1: Java & Spring] 08 Project Panama Ffi`
- `@ [M1: Java & Spring] 09 Estructuras Probabilisticas En Memoria`
- `@ [M1: Java & Spring] 10 Verificacion Formal Y Seguridad`
- `@ [M1: Java & Spring] 11 Ingestion Udp Zero Copy`
- `@ [M1: Java & Spring] 11 Rutas Aprendizaje Java Spring`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los documentos referenciados arriba (@ [M1: Java & Spring]). Ignora cualquier otra fuente o archivo no listado.

Actúa como un Distinguished Principal Engineer en Java y docente de nivel MIT/Carnegie Mellon. Utilizando como contexto EXCLUSIVO la documentación etiquetada arriba del módulo "M1: Java & Spring", genera un Cuaderno Formativo (Notebook) exhaustivo sobre el Lenguaje Java (Java 25 LTS) y Spring Boot 4.0.

El Cuaderno debe incluir las siguientes secciones detalladas:
1. Sintaxis Moderna & Concurrencia Ligera: Explicación y snippets ejecutables de Java 25 Records, Pattern Matching exhaustivo, Scoped Values, Virtual Threads (Project Loom) y prevención de Carrier Thread Pinning.
2. Optimización del Runtime & Cold-Start: Análisis de Project Leyden, entrenamiento y generación de CDS (Class Data Sharing .jsa), compatibilidad AOT con GraalVM Native Image y Generational ZGC.
3. Arquitectura de Infraestructura en Spring Boot 4.0: Configuración modular bajo Java 25, propagación de contexto en hilos virtuales y cero reflexión dinámica innecesaria.
4. Ejercicios Prácticos y Casos de Estudio: 3 problemas de concurrencia masiva e ingesta de datos con sus soluciones paso a paso en Java 25 puro.
5. Preguntas de Autoevaluación & Flashcards de Examen.
```

---

### 🐹 Prompt 2: Cuaderno de Estudio del Lenguaje Go (Go 1.24 & Concurrencia de Alta Velocidad)

**📄 Documentos de Referencia Exclusivos (Módulo 2):**
- `@ [M2: Go & Concurrencia] 01 Arquitectura Y Runtime Go`
- `@ [M2: Go & Concurrencia] 02 Modelo De Concurrencia Csp`
- `@ [M2: Go & Concurrencia] 03 Gestion De Memoria Y Gc Go`
- `@ [M2: Go & Concurrencia] 04 Patrones Avanzados Concurrencia Go`
- `@ [M2: Go & Concurrencia] 05 Programacion Competitiva Y Optimizacion Extrema`
- `@ [M2: Go & Concurrencia] 06 Rutas Aprendizaje Go`
- `@ [M2: Go & Concurrencia] 01 Go Fundamentos Desde Cero`
- `@ [M2: Go & Concurrencia] 01 Go Concurrency Memory Opt`
- `@ [M2: Go & Concurrencia] 02 Go Concurrency Memory Opt`
- `@ [M2: Go & Concurrencia] 02 Resilience Circuit Breaker Chaos`
- `@ [M2: Go & Concurrencia] 03 Resilience Circuit Breaker Chaos`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los documentos referenciados arriba (@ [M2: Go & Concurrencia]). Ignora cualquier otra fuente o archivo no listado.

Actúa como un Core Developer de Go y Arquitecto de Sistemas de Baja Latencia. Utilizando como contexto EXCLUSIVO los documentos del módulo "M2: Go & Concurrencia" referenciados arriba, genera un Cuaderno Formativo (Notebook) completo sobre el Lenguaje Go (Go 1.24+).

El Cuaderno debe estructurarse en los siguientes apartados:
1. Fundamentos y Modelo de Concurrencia CSP: Canales (buffered/unbuffered), Goroutines, instrucción select, mutexes sync/atomic y patrones fan-out/fan-in de alto rendimiento.
2. Runtime, GC & Zero-Allocation: Análisis del GC tri-color de Go, escape analysis (`go build -gcflags="-m"`), reutilización de buffers con `sync.Pool` y optimización de allocations a O(0).
3. Aplicaciones de Red y Ruteo Geoespacial: Implementación de servicios de despacho físico usando algoritmos de Contraction Hierarchies (OSRM) y Workers multihilo deterministas.
4. Resiliencia & Chaos Testing: Implementación de Circuit Breakers predictivos y pruebas de caos de red.
5. Banco de Pruebas y Preguntas Clave: 5 preguntas de nivel Staff Engineer sobre Go Runtime y solución de 2 race conditions reales.
```

---

### 🐍 Prompt 3: Cuaderno de Estudio del Lenguaje Python (IA, Computación Científica & Simulación)

**📄 Documentos de Referencia Exclusivos (Módulo 3 - Python & IA):**
- `@ [M3: Unified Twin & Math] 01 Fundamentos Algebra Tensorial Numpy`
- `@ [M3: Unified Twin & Math] 05 Mesa Abm Agent Simulations`
- `@ [M3: Unified Twin & Math] 06 Pypsa Power Network Optimizer`
- `@ [M3: Unified Twin & Math] 07 Genetic Algorithms Nsga2 Petri`
- `@ [M3: Unified Twin & Math] 09 Arquitectura Transformers`
- `@ [M3: Unified Twin & Math] 10 Gemelo Digital Unificado Core`
- `@ [M3: Unified Twin & Math] 12 Pinns Water Hammer`
- `@ [M3: Unified Twin & Math] 12 Rutas Aprendizaje Python Ia Simulaciones`
- `@ [M3: Unified Twin & Math] 13 Sinergias Multidominio Opf Vrp`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los documentos referenciados arriba (@ [M3: Unified Twin & Math - Python]). Ignora cualquier otra fuente o archivo no listado.

Actúa como un Senior Research Scientist en Computación Cuantitativa e Inteligencia Artificial. Con base EXCLUSIVA en los documentos de Python, IA y simulaciones listados arriba, genera un Cuaderno Formativo (Notebook) intensivo sobre el Lenguaje Python.

El Cuaderno debe estructurarse de la siguiente forma:
1. Vectorización Extrema & Performance: Transformación de bucles lentos `for` en código vectorizado acelerado con NumPy/SciPy/Cupy, uso de `__slots__` y generadores para optimizar memoria RAM.
2. Modelado Científico & Simulación: Uso de SymPy para derivación simbólica, PyPSA para flujo de potencia (LPOPF), Mesa para Simulaciones Basadas en Agentes (ABM) y PINNs (Physics-Informed Neural Networks).
3. Grafo Tensorial Unificado & Asimilación: Inyección de tensores al núcleo `tensor_gnn_core.py` y filtros de Kalman EnKF en Python.
4. Guía de Refactorización: 3 casos de estudio donde se pasa de código Python ineficiente O(N^2) a operaciones vectorizadas O(N log N) o O(1).
5. Test de Validación Teórica y Cuestionario de Repaso.
```

---

### 🎯 Prompt 4: Cuaderno de Estudio del Lenguaje Dart & Framework Flutter (UI Móvil & Multiplataforma)

**📄 Documentos de Referencia Exclusivos (Módulo 4 - Flutter & Dart):**
- `@ [M4: Frontend & UI Engine] 02 Arquitectura Flutter Y Skia`
- `@ [M4: Frontend & UI Engine] 06 Rutas Aprendizaje Flutter React`
- `@ [M4: Frontend & UI Engine] 01 H3 Spatial Indexing Surge`
- `@ [M4: Frontend & UI Engine] 02 Dart Flutter Fundamentos Desde Cero`
- `@ [M4: Frontend & UI Engine] 02 Osrm Routing Flutter Clean`
- `@ [M4: Frontend & UI Engine] 03 H3 Spatial Indexing Surge`
- `@ [M4: Frontend & UI Engine] 04 Osrm Routing Flutter Clean`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los documentos referenciados arriba (@ [M4: Frontend & UI Engine - Dart/Flutter]). Ignora cualquier otra fuente o archivo no listado.

Actúa como un Principal Mobile Architect experto en Flutter, Dart y sistemas offline-first. Basándote EXCLUSIVAMENTE en la documentación referenciada arriba sobre Dart y Flutter, genera un Cuaderno Formativo (Notebook) completo sobre el Lenguaje Dart y Flutter.

El Cuaderno debe abordar:
1. Lenguaje Dart 3.x Avanzado: Pattern Matching, Destructuring, Sealed Classes, Extension Types, FFI (Foreign Function Interface) con C/C++ y compilación Ahead-Of-Time (AOT).
2. Motor de Renderizado Flutter (Skia/Impeller): Gestión de RenderObjects, optimización de Rebuilds, prevención de Jank (60/120 FPS) e indexación espacial Uber H3 integrada en UI.
3. Arquitectura Clean & Offline-First: Separación en capas (UI, Domain, Data), SQLite local, persistencia resiliente y sincronización adaptativa en segundo plano.
4. Laboratorio Práctico: Implementación paso a paso de un mapa de calor geográfico basado en celdas H3 con refresco adaptativo.
5. Preguntas Frecuentes de Arquitectura Móvil & Checklist de Rendimiento.
```

---

## 🏛️ SECCIÓN B: CUADERNOS POR ÁREAS FORMATIVAS DEL ECOSISTEMA

---

### 🏭 Prompt 5: Cuaderno de Ingeniería Industrial, Investigación Operativa & Simulación DES

**📄 Documentos de Referencia Exclusivos (Módulo 0 & Módulo 3):**
- `@ [M0: Ing. Industrial] 01 Ramas Ingenieria Industrial`
- `@ [M3: Unified Twin & Math] 03 Des Queue Theory Poisson`
- `@ [M3: Unified Twin & Math] 04 Des Queue Theory Poisson`
- `@ [M3: Unified Twin & Math] 11 Simulaciones Hpc Y Multiescala`
- `@ [M3: Unified Twin & Math] 13 Sinergias Multidominio Opf Vrp`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 5 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como Catedrático de Ingeniería Industrial y Logística Cuantitativa (Georgia Tech / TU Delft). Utilizando EXCLUSIVAMENTE la documentación referenciada arriba sobre ingeniería industrial y teoría de colas, crea un Cuaderno de Formación Superior sobre Ingeniería Industrial e Investigación Operativa.

El Cuaderno debe incluir:
1. Teoría de Colas & Procesos Estocásticos: Modelos M/M/1, M/M/c, distribución de Poisson, colas M/G/k y cálculo de buffers de tolerancia para evitar cuellos de botella en nudos de transporte.
2. Simulación de Eventos Discretos (DES): Diseño de motores de eventos en tiempo discreto, gestión de calendarios de eventos y análisis de estado estacionario vs transitorio.
3. Optimización de Flotas y VRP Estocástico: Formulaciones matemáticas para el Vehicle Routing Problem con ventanas de tiempo (VRPTW) e incertidumbre operacional.
4. Caso Práctico Empresarial: Modelo completo resuelto de balanceo de línea de producción y gestión de inventario Justin-In-Time (JIT).
5. Evaluación Teórico-Práctica.
```

---

### 🌐 Prompt 6: Cuaderno de Sistemas Distribuidos, Consenso & Verificación Formal

**📄 Documentos de Referencia Exclusivos (Módulo 0 & Módulo 6):**
- `@ [M0: Sist. Distribuidos] 01 Modelos De Sistemas Distribuidos`
- `@ [M0: Sist. Distribuidos] 02 Relojes Logicos Y Lamport`
- `@ [M0: Sist. Distribuidos] 03 Algoritmos De Eleccion De Lider`
- `@ [M0: Sist. Distribuidos] 04 Topologias De Red Datacenter`
- `@ [M0: Sist. Distribuidos] 05 Consenso Distribuido Avanzado`
- `@ [M0: Sist. Distribuidos] 06 Verificacion Formal Tla`
- `@ [M6: SRE & Alta Disponibilidad] 02 Teorema Cap Y Pacelc`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 7 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como un Investigador Principal de Sistemas Distribuidos (CMU / ETH Zurich). Basándote EXCLUSIVAMENTE en los documentos de Sistemas Distribuidos listados arriba, construye un Cuaderno Formativo de Nivel Avanzado sobre Sistemas Distribuidos.

El Cuaderno debe contener:
1. Modelos de Fallo & Tiempo: Redes asíncronas vs síncronas, relojes lógicos de Lamport, vector clocks y detección de corte consistente (Chandy-Lamport).
2. Algoritmos de Consenso: Estudio comparativo profundo entre Paxos, Raft y Zab. Análisis de quorum, elecciones de líder y replicación de logs.
3. Limites Teóricos (CAP / PACELC): Implicaciones de consistencia fuerte vs eventual en bases de datos distribuidas multi-region.
4. Verificación Formal con TLA+: Especificación de propiedades de Invarianza (Safety) y Livenanza (Liveness) con PlusCal / TLA+.
5. Examen de Autoevaluación & Análisis de Postmortems Distribuidos.
```

---

### 🏛️ Prompt 7: Cuaderno de Ingeniería de Software, Arquitectura Hexagonal & DDD

**📄 Documentos de Referencia Exclusivos (Módulo 0 & Módulo 6):**
- `@ [M0: Software Eng.] 00 Ingenieria De Sistemas Y Software`
- `@ [M0: Software Eng.] 01 Arquitectura Hexagonal Ddd Puro`
- `@ [M0: Software Eng.] 02 Tdd Zero Mockito Testcontainers`
- `@ [M0: Software Eng.] 03 Documentacion Adrs Conocimiento`
- `@ [M0: Software Eng.] 03 Toyota Kata Sdlc 6 Fases`
- `@ [M0: Software Eng.] 04 Compliance Gdpr Ai Act Pii`
- `@ [M0: Software Eng.] 06 Economia Del Software Y Deuda Tecnica`
- `@ [M0: Software Eng.] 07 Calidad De Software Y Qa Testing`
- `@ [M6: SRE & Alta Disponibilidad] 03 Arquitectura Hexagonal Y Ddd`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 9 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como un Software Architecture Fellow y autor referente en DDD. Con la documentación EXCLUSIVA referenciada arriba sobre ingeniería de software y arquitectura limpia, genera un Cuaderno Formativo sobre Ingeniería de Software y Arquitectura de Dominio Puro.

El Cuaderno debe detallar:
1. Arquitectura Hexagonal (Ports & Adapters): Separación estricta entre Dominio Puro (`domain/`), Puertos de Entrada/Salida y Adaptadores de Infraestructura.
2. Domain-Driven Design (DDD): Entidades, Records inmutables, Value Objects, Agregados, Domain Events y Bounded Contexts sin contaminación de frameworks.
3. Calidad & Prove-It Standard: TDD riguroso, Zero Mockito en el dominio, integración con Testcontainers e inspección adversarial pre-commit.
4. Cadena de Suministro Segura: Generación de proveniencia SLSA Nivel 3/4 y firma de atestaciones con Sigstore/Cosign.
5. Cuestionario de Arquitectura & Tareas de Refactorización.
```

---

### 🧮 Prompt 8: Cuaderno de Matemáticas Avanzadas, Física & Gemelo Digital Unificado

**📄 Documentos de Referencia Exclusivos (Módulo 3):**
- `@ [M3: Unified Twin & Math] 01 Calculo Tensorial Y Geometria`
- `@ [M3: Unified Twin & Math] 01 Tensor Networks Peps`
- `@ [M3: Unified Twin & Math] 02 Fisica De Fluidos Navier Stokes`
- `@ [M3: Unified Twin & Math] 02 Kalman Filter Enkf`
- `@ [M3: Unified Twin & Math] 03 Asimilacion De Datos Enkf`
- `@ [M3: Unified Twin & Math] 04 Calculo Estocastico Ito`
- `@ [M3: Unified Twin & Math] 04 Svd Edge Litert Fragmentation`
- `@ [M3: Unified Twin & Math] 05 Teoria De Grafos Espectral`
- `@ [M3: Unified Twin & Math] 06 Optimizacion No Lineal Kkt`
- `@ [M3: Unified Twin & Math] 07 Teoria De Juegos Y Mercados`
- `@ [M3: Unified Twin & Math] 08 Filtro De Kalman No Lineal`
- `@ [M3: Unified Twin & Math] 09 Fisica Clima Navier Stokes Pde`
- `@ [M3: Unified Twin & Math] 10 Gemelo Digital Unificado Core`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 13 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como un Investigador del Institute for Advanced Study (Princeton) y especialista en Física Computacional. Basándote EXCLUSIVAMENTE en los 13 documentos matemáticos y físicos listados arriba, elabora un Cuaderno Formativo sobre Matemáticas Complejas y el Gemelo Digital Unificado.

El Cuaderno debe abordar:
1. Álgebra Tensorial & PEPS: Representación tensorial de sistemas complejos, descomposición SVD e interpolación tensorial para modelos reducidos (ROM).
2. Asimilación de Datos & Filtro de Kalman EnKF: Ecuaciones de actualización estocástica, matrices de covarianza y convergencia por debajo de 0.5.
3. Física de Fluidos & EDPs: Ecuaciones de Navier-Stokes, golpe de ariete (Water Hammer) modelado con Physics-Informed Neural Networks (PINNs).
4. El Motor Unificado (Unified Twin Core): Arquitectura e integración de pertubaciones al Gemelo Digital corporativo.
5. Problemas Matemáticos Resueltos & Desafíos Teóricos.
```

---

### 🎨 Prompt 9: Cuaderno de Frontend Web, Motores UI & Core Web Vitals

**📄 Documentos de Referencia Exclusivos (Módulo 4 - Web & React):**
- `@ [M4: Frontend & UI Engine] 01 Arquitectura React Y Virtual Dom`
- `@ [M4: Frontend & UI Engine] 03 Web Performance Y Core Web Vitals`
- `@ [M4: Frontend & UI Engine] 04 Patrones Offline First Y Pwa`
- `@ [M4: Frontend & UI Engine] 05 Hci E Interfaces Cognitivas`
- `@ [M4: Frontend & UI Engine] 07 Diseno Ui Ux Y Sistemas De Diseno`
- `@ [M4: Frontend & UI Engine] 01 React Tailwind Pwa Fundamentos`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 6 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como un Lead Web Performance Engineer de Google. Con base EXCLUSIVA en los 6 documentos de Frontend Web y React listados arriba, redacta un Cuaderno Formativo sobre Desarrollo Frontend Moderno y Optimización Web.

El Cuaderno debe incluir:
1. Arquitectura React & Virtual DOM: Reconciliation, Fiber tree, Server Components, Hooks y gestión eficiente del estado.
2. Core Web Vitals & Rendimiento Web: Métricas LCP (Largest Contentful Paint), INP (Interaction to Next Paint), CLS (Cumulative Layout Shift) y técnicas de optimización visual.
3. Patrones PWA & Offline-First: Service Workers, Cache Storage API, Background Sync y persistencia local sin degradación.
4. Accesibilidad (WCAG 2.2 AA) & UI/UX: Ergonomía cognitiva, sistemas de diseño dinámicos con tokens OKLCH y diseño responsivo.
5. Checklist Auditoría Web & Ejercicios de Diagnóstico.
```

---

### ☁️ Prompt 10: Cuaderno de Infraestructura Cloud-Native, GCP & Serverless

**📄 Documentos de Referencia Exclusivos (Módulo 5):**
- `@ [M5: Cloud-Native & GCP] 01 Contenedores Linux Cgroups Namespaces`
- `@ [M5: Cloud-Native & GCP] 02 Kubernetes Internals Etcd Raft`
- `@ [M5: Cloud-Native & GCP] 03 Arquitectura Serverless Cloud Run`
- `@ [M5: Cloud-Native & GCP] 04 Infraestructura Como Codigo Terraform`
- `@ [M5: Cloud-Native & GCP] 05 Arquitecturas Dataflow Y Serverless Distribuido`
- `@ [M5: Cloud-Native & GCP] 06 Rutas Aprendizaje Gcp Cloud Native`
- `@ [M5: Cloud-Native & GCP] 01 Gcp Cloud Run Tasks Firestore`
- `@ [M5: Cloud-Native & GCP] 01 Gcp Fundamentos Cloud Run Tasks`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 8 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como un Principal Cloud Architect (GCP Champion). Basándote EXCLUSIVAMENTE en la documentación referenciada arriba sobre Infraestructura y GCP, genera un Cuaderno Formativo sobre Infraestructura Cloud-Native y Plataformas GCP.

El Cuaderno abarcará:
1. Contenedores Linux & Runtime: Namespaces, Cgroups v2, aislación de procesos y arquitectura interna de Kubernetes (etcd, kube-apiserver, Scheduler).
2. Serverless Masivo en GCP: Cloud Run, Cloud Tasks, Firestore, triggers asíncronos y optimización FinOps para escala $0.015 USD/MAU/mes.
3. Infraestructura como Código (IaC): Terraform declarativo, GitOps con ArgoCD y gestión inmutable del estado.
4. Casos de Estudio de Despliegue: Arquitectura multirregión con alta disponibilidad y escalado automático a cero.
5. Preguntas de Examen de Certificación Cloud Architect.
```

---

### 🛡️ Prompt 11: Cuaderno de SRE, Resiliencia & Telemetría OpenTelemetry

**📄 Documentos de Referencia Exclusivos (Módulo 6):**
- `@ [M6: SRE & Alta Disponibilidad] 01 Sre Slis Slos Error Budgets`
- `@ [M6: SRE & Alta Disponibilidad] 04 Patrones De Resiliencia Circuit Breaker`
- `@ [M6: SRE & Alta Disponibilidad] 05 Observabilidad Y Telemetria Otel`
- `@ [M6: SRE & Alta Disponibilidad] 05 Predictive Circuit Breakers`
- `@ [M6: SRE & Alta Disponibilidad] 06 Despliegues Canary Y Blue Green`
- `@ [M6: SRE & Alta Disponibilidad] 07 Bases De Datos Distribuidas`
- `@ [M6: SRE & Alta Disponibilidad] 08 Gestion De Incidentes Y Postmortems`
- `@ [M6: SRE & Alta Disponibilidad] 09 Ingenieria De Software Empirica`
- `@ [M6: SRE & Alta Disponibilidad] 10 Zero To Hero Project Bootstrap`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 9 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como un Google SRE Site Reliability Director. Basándote EXCLUSIVAMENTE en los 9 documentos de SRE y Alta Disponibilidad referenciados arriba, redacta un Cuaderno Formativo sobre SRE, Observabilidad y Resiliencia de Sistemas.

El Cuaderno cubrirá:
1. Métricas de Fiabilidad: Definiendo SLIs (Service Level Indicators), SLOs (Service Level Objectives) y gestión estratégica de Error Budgets.
2. Observabilidad de Extremo a Extremo: Tracing distribuido con OpenTelemetry (OTEL), métricas Prometheus y agregación estructurada de logs.
3. Patrones de Resiliencia: Circuit Breakers predictivos, Rate Limiting, Shedding de carga y aislamiento por Mamparas (Bulkheading).
4. Gestión de Incidentes & Postmortems: Cultura blameless, análisis de causa raíz y planes de mitigación de emergencia.
5. Ejercicios Prácticos de Simulación de Incidencias & Evaluación SRE.
```

---

### 🗄️ Prompt 12: Cuaderno de Bases de Datos NoSQL, OLAP & Multi-Tenancy Segura

**📄 Documentos de Referencia Exclusivos (Módulo 7 & Fintech):**
- `@ [M7: NoSQL & Multi-Tenancy] 01 Olap Bigquery Arquitectura Columnar`
- `@ [M7: NoSQL & Multi-Tenancy] 02 Firestore Internals Y Alta Concurrencia`
- `@ [M7: NoSQL & Multi-Tenancy] 03 Patrones Multi Tenancy Y Rls`
- `@ [M5: Cloud-Native & GCP] 02 Bigquery Gql Finops`
- `@ [M5: Cloud-Native & GCP] 02 Bigquery Sql Bqml Gql Finops`
- `@ [M5: Cloud-Native & GCP] 03 Stripe Connect Idempotency Escrow`
- `@ [M5: Cloud-Native & GCP] 04 Vertex Ai Rag Json Structured Output`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE los 7 documentos referenciados arriba. Ignora cualquier otra fuente o archivo no listado.

Actúa como un Chief Data Architect experto en almacenamiento distribuido y seguridad multi-inquilino. Con la información EXCLUSIVA de los 7 documentos referenciados arriba sobre NoSQL, BigQuery y Fintech, construye un Cuaderno Formativo sobre Bases de Datos y Multi-Tenancy.

El Cuaderno debe estructurarse en:
1. Almacenamiento Columnar OLAP (BigQuery): Internals de Dremel, partitioning por fecha, clustering por `tenant_id` y ejecuciones costo-eficientes con BQML.
2. Bases de Datos Documentales NoSQL (Firestore): Arquitectura de índices, alta concurrencia en lecturas/escrituras y diseño de colecciones atómicas.
3. Aislamiento Multi-Tenant & RLS: Firestore Security Rules, Row-Level Security en SQL y prevención estricta de fugas de datos entre inquilinos.
4. Integración Fintech: Idempotencia transaccional con Stripe Connect, patron Saga y cuentas de custodia (Escrow).
5. Examen Teórico-Práctico de Seguridad e Integridad de Datos.
```
"""

with open(PROMPTS_FILE, "w", encoding="utf-8") as f:
    f.write(content)

print("Prompts file updated locally with explicit document references.")

def split_text_chunks(text, max_len=1900):
    return [text[i:i+max_len] for i in range(0, len(text), max_len)]

blocks = []
lines = content.splitlines()
in_code = False
code_lines = []

for line in lines:
    stripped = line.strip()
    if stripped.startswith("```"):
        if in_code:
            full_code = "\n".join(code_lines)
            chunks = split_text_chunks(full_code, 1900)
            for chunk in chunks:
                blocks.append({
                    "object": "block",
                    "type": "code",
                    "code": {
                        "rich_text": [{"type": "text", "text": {"content": chunk}}],
                        "language": "plain text"
                    }
                })
            code_lines = []
            in_code = False
        else:
            in_code = True
        continue
    
    if in_code:
        code_lines.append(line)
        continue

    if not stripped: continue

    if stripped.startswith("# "):
        blocks.append({"object": "block", "type": "heading_1", "heading_1": {"rich_text": [{"type": "text", "text": {"content": stripped[2:]}}]}})
    elif stripped.startswith("## "):
        blocks.append({"object": "block", "type": "heading_2", "heading_2": {"rich_text": [{"type": "text", "text": {"content": stripped[3:]}}]}})
    elif stripped.startswith("### "):
        blocks.append({"object": "block", "type": "heading_3", "heading_3": {"rich_text": [{"type": "text", "text": {"content": stripped[4:]}}]}})
    elif stripped.startswith("> "):
        blocks.append({"object": "block", "type": "callout", "callout": {"rich_text": [{"type": "text", "text": {"content": stripped[2:]}}], "icon": {"emoji": "💡"}}})
    elif stripped.startswith("- ") or stripped.startswith("* "):
        blocks.append({"object": "block", "type": "bulleted_list_item", "bulleted_list_item": {"rich_text": [{"type": "text", "text": {"content": stripped[2:]}}]}})
    elif len(stripped) > 2 and stripped[0].isdigit() and stripped[1] in [".", ")"]:
        parts = stripped.split(" ", 1)
        text = parts[1].strip() if len(parts) > 1 else stripped
        blocks.append({"object": "block", "type": "numbered_list_item", "numbered_list_item": {"rich_text": [{"type": "text", "text": {"content": text}}]}})
    else:
        for chunk in split_text_chunks(stripped, 1900):
            blocks.append({"object": "block", "type": "paragraph", "paragraph": {"rich_text": [{"type": "text", "text": {"content": chunk}}]}})

# Limpiar los bloques actuales de la página de Prompts en Notion
print(f"Limpiando bloques anteriores de la página Notion {PROMPTS_PAGE_ID}...")
children_req = urllib.request.Request(
    f"https://api.notion.com/v1/blocks/{PROMPTS_PAGE_ID}/children",
    headers={
        "Authorization": f"Bearer {NOTION_TOKEN}",
        "Notion-Version": NOTION_VERSION
    }
)
try:
    with urllib.request.urlopen(children_req) as resp:
        res = json.loads(resp.read().decode("utf-8"))
        for b in res.get("results", []):
            bid = b["id"]
            del_req = urllib.request.Request(
                f"https://api.notion.com/v1/blocks/{bid}",
                method="DELETE",
                headers={
                    "Authorization": f"Bearer {NOTION_TOKEN}",
                    "Notion-Version": NOTION_VERSION
                }
            )
            try:
                with urllib.request.urlopen(del_req) as dresp: pass
            except Exception: pass
except Exception as e:
    print("Error en limpieza:", e)

# Subir bloques actualizados
print("Subiendo bloques actualizados a Notion Prompts Page...")
remaining_blocks = blocks[:]
while remaining_blocks:
    time.sleep(0.35)
    batch = remaining_blocks[:95]
    remaining_blocks = remaining_blocks[95:]
    app_req = urllib.request.Request(
        f"https://api.notion.com/v1/blocks/{PROMPTS_PAGE_ID}/children",
        method="PATCH",
        headers={
            "Authorization": f"Bearer {NOTION_TOKEN}",
            "Notion-Version": NOTION_VERSION,
            "Content-Type": "application/json"
        },
        data=json.dumps({"children": batch}).encode("utf-8")
    )
    with urllib.request.urlopen(app_req) as resp:
        pass

print("Página de Prompts en Notion Actualizada Exitosamente con Referencias Exclusivas!")
