# Prompts de Notion AI para la Generación de Cuadernos Formativos (Notebooks)
## Proyecto: Starter Común — Basado Únicamente en README.md y Master Book PDF

> [!IMPORTANT]
> **Instrucciones de Referencia Exclusiva en Notion AI:**
> Para garantizar que Notion AI se base **ÚNICAMENTE en el README.md principal y el Libro Máster CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf**:
> 1. En la casilla de consulta de Notion AI, utiliza la mención `@` para seleccionar los dos documentos maestros de referencia:
>    - `@ [REFERENCIA PRINCIPAL] README.md`
>    - `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`
> 2. Copia y pega el bloque del Prompt deseado.

---

## 📚 SECCIÓN A: LENGUAJES DE PROGRAMACIÓN (ÁREAS INDEPENDIENTES)

---

### ☕ Prompt 1: Cuaderno de Estudio del Lenguaje Java (Java 25 LTS & Spring Boot 4.1)

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Distinguished Principal Engineer en Java y docente de nivel MIT/Carnegie Mellon. Extrayendo la información relevante ÚNICAMENTE del Libro Máster PDF y el README principal, genera un Cuaderno Formativo (Notebook) exhaustivo enfocado en el Lenguaje Java (Java 25 LTS) y Spring Boot 4.1 (Módulo 1).

El Cuaderno debe incluir las siguientes secciones detalladas:
1. Sintaxis Moderna & Concurrencia Ligera: Explicación y snippets ejecutables de Java 25 Records, Pattern Matching exhaustivo, Scoped Values, Virtual Threads (Project Loom) y prevención de Carrier Thread Pinning.
2. Optimización del Runtime & Cold-Start: Análisis de Project Leyden, entrenamiento y generación de CDS (Class Data Sharing .jsa), compatibilidad AOT con GraalVM Native Image y Generational ZGC.
3. Arquitectura de Infraestructura en Spring Boot 4.1: Configuración modular bajo Java 25, propagación de contexto en hilos virtuales y cero reflexión dinámica innecesaria.
4. Ejercicios Prácticos y Casos de Estudio: 3 problemas de concurrencia masiva e ingesta de datos con sus soluciones paso a paso en Java 25 puro.
5. Preguntas de Autoevaluación & Flashcards de Examen.
```

---

### 🐹 Prompt 2: Cuaderno de Estudio del Lenguaje Go (Go 1.24 & Concurrencia de Alta Velocidad)

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Core Developer de Go y Arquitecto de Sistemas de Baja Latencia. Extrayendo la información relevante ÚNICAMENTE del Libro Máster PDF y el README principal, genera un Cuaderno Formativo (Notebook) completo enfocado en el Lenguaje Go (Go 1.24+, Módulo 2).

El Cuaderno debe estructurarse en los siguientes apartados:
1. Fundamentos y Modelo de Concurrencia CSP: Canales (buffered/unbuffered), Goroutines, instrucción select, mutexes sync/atomic y patrones fan-out/fan-in de alto rendimiento.
2. Runtime, GC & Zero-Allocation: Análisis del GC tri-color de Go, escape analysis (`go build -gcflags="-m"`), reutilización de buffers con `sync.Pool` y optimización de allocations a O(0).
3. Aplicaciones de Red y Ruteo Geoespacial: Implementación de servicios de despacho físico usando algoritmos de Contraction Hierarchies (OSRM) y Workers multihilo deterministas.
4. Resiliencia & Chaos Testing: Implementación de Circuit Breakers predictivos y pruebas de caos de red.
5. Banco de Pruebas y Preguntas Clave: 5 preguntas de nivel Staff Engineer sobre Go Runtime y solución de 2 race conditions reales.
```

---

### 🐍 Prompt 3: Cuaderno de Estudio del Lenguaje Python (IA, Computación Científica & Simulación)

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Senior Research Scientist en Computación Cuantitativa e Inteligencia Artificial. Extrayendo la información relevante ÚNICAMENTE de las secciones de Python, IA y matemáticas del Libro Máster PDF y del README principal, genera un Cuaderno Formativo (Notebook) intensivo sobre el Lenguaje Python.

El Cuaderno debe estructurarse de la siguiente forma:
1. Vectorización Extrema & Performance: Transformación de bucles lentos `for` en código vectorizado acelerado con NumPy/SciPy/Cupy, uso de `__slots__` y generadores para optimizar memoria RAM.
2. Modelado Científico & Simulación: Uso de SymPy para derivación simbólica, PyPSA para flujo de potencia (LPOPF), Mesa para Simulaciones Basadas en Agentes (ABM) y PINNs (Physics-Informed Neural Networks).
3. Grafo Tensorial Unificado & Asimilación: Inyección de tensores al núcleo `tensor_gnn_core.py` y filtros de Kalman EnKF en Python.
4. Guía de Refactorización: 3 casos de estudio donde se pasa de código Python ineficiente O(N^2) a operaciones vectorizadas O(N log N) o O(1).
5. Test de Validación Teórica y Cuestionario de Repaso.
```

---

### 🎯 Prompt 4: Cuaderno de Estudio del Lenguaje Dart & Framework Flutter (UI Móvil & Multiplataforma)

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Principal Mobile Architect experto en Flutter, Dart y sistemas offline-first. Extrayendo la información relevante ÚNICAMENTE de los apartados de Frontend y Movilidad H3 del Libro Máster PDF y del README principal, genera un Cuaderno Formativo (Notebook) completo sobre el Lenguaje Dart y Flutter.

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

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como Catedrático de Ingeniería Industrial y Logística Cuantitativa (Georgia Tech / TU Delft). Extrayendo EXCLUSIVAMENTE los capítulos de ingeniería industrial y teoría de colas del Libro Máster PDF y del README principal, crea un Cuaderno de Formación Superior sobre Ingeniería Industrial e Investigación Operativa.

El Cuaderno debe incluir:
1. Teoría de Colas & Procesos Estocásticos: Modelos M/M/1, M/M/c, distribución de Poisson, colas M/G/k y cálculo de buffers de tolerancia para evitar cuellos de botella en nudos de transporte.
2. Simulación de Eventos Discretos (DES): Diseño de motores de eventos en tiempo discreto, gestión de calendarios de eventos y análisis de estado estacionario vs transitorio.
3. Optimización de Flotas y VRP Estocástico: Formulaciones matemáticas para el Vehicle Routing Problem con ventanas de tiempo (VRPTW) e incertidumbre operacional.
4. Caso Práctico Empresarial: Modelo completo resuelto de balanceo de línea de producción y gestión de inventario Justin-In-Time (JIT).
5. Evaluación Teórico-Práctica.
```

---

### 🌐 Prompt 6: Cuaderno de Sistemas Distribuidos, Consenso & Verificación Formal

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Investigador Principal de Sistemas Distribuidos (CMU / ETH Zurich). Extrayendo EXCLUSIVAMENTE los capítulos de sistemas distribuidos y consensos del Libro Máster PDF y del README principal, construye un Cuaderno Formativo de Nivel Avanzado sobre Sistemas Distribuidos.

El Cuaderno debe contener:
1. Modelos de Fallo & Tiempo: Redes asíncronas vs síncronas, relojes lógicos de Lamport, vector clocks y detección de corte consistente (Chandy-Lamport).
2. Algoritmos de Consenso: Estudio comparativo profundo entre Paxos, Raft y Zab. Análisis de quorum, elecciones de líder y replicación de logs.
3. Limites Teóricos (CAP / PACELC): Implicaciones de consistencia fuerte vs eventual en bases de datos distribuidas multi-region.
4. Verificación Formal con TLA+: Especificación de propiedades de Invarianza (Safety) y Livenanza (Liveness) con PlusCal / TLA+.
5. Examen de Autoevaluación & Análisis de Postmortems Distribuidos.
```

---

### 🏛️ Prompt 7: Cuaderno de Ingeniería de Software, Arquitectura Hexagonal & DDD

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Software Architecture Fellow y autor referente en DDD. Extrayendo EXCLUSIVAMENTE las secciones de arquitectura de software, DDD puro y calidad del Libro Máster PDF y del README principal, genera un Cuaderno Formativo sobre Ingeniería de Software y Arquitectura de Dominio Puro.

El Cuaderno debe detallar:
1. Arquitectura Hexagonal (Ports & Adapters): Separación estricta entre Dominio Puro (`domain/`), Puertos de Entrada/Salida y Adaptadores de Infraestructura.
2. Domain-Driven Design (DDD): Entidades, Records inmutables, Value Objects, Agregados, Domain Events y Bounded Contexts sin contaminación de frameworks.
3. Calidad & Prove-It Standard: TDD riguroso, Zero Mockito en el dominio, integración con Testcontainers e inspección adversarial pre-commit.
4. Cadena de Suministro Segura: Generación de proveniencia SLSA Nivel 3/4 y firma de atestaciones con Sigstore/Cosign.
5. Cuestionario de Arquitectura & Tareas de Refactorización.
```

---

### 🧮 Prompt 8: Cuaderno de Matemáticas Avanzadas, Física & Gemelo Digital Unificado

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Investigador del Institute for Advanced Study (Princeton) y especialista en Física Computacional. Extrayendo EXCLUSIVAMENTE el Módulo 3 de matemáticas avanzadas, redes tensoriales y física del Libro Máster PDF y del README principal, elabora un Cuaderno Formativo sobre Matemáticas Complejas y el Gemelo Digital Unificado.

El Cuaderno debe abordar:
1. Álgebra Tensorial & PEPS: Representación tensorial de sistemas complejos, descomposición SVD e interpolación tensorial para modelos reducidos (ROM).
2. Asimilación de Datos & Filtro de Kalman EnKF: Ecuaciones de actualización estocástica, matrices de covarianza y convergencia por debajo de 0.5.
3. Física de Fluidos & EDPs: Ecuaciones de Navier-Stokes, golpe de ariete (Water Hammer) modelado con Physics-Informed Neural Networks (PINNs).
4. El Motor Unificado (Unified Twin Core): Arquitectura e integración de pertubaciones al Gemelo Digital corporativo.
5. Problemas Matemáticos Resueltos & Desafíos Teóricos.
```

---

### 🎨 Prompt 9: Cuaderno de Frontend Web, Motores UI & Core Web Vitals

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Lead Web Performance Engineer de Google. Extrayendo EXCLUSIVAMENTE el Módulo 4 de Frontend, React y rendimiento web del Libro Máster PDF y del README principal, redacta un Cuaderno Formativo sobre Desarrollo Frontend Moderno y Optimización Web.

El Cuaderno debe incluir:
1. Arquitectura React & Virtual DOM: Reconciliation, Fiber tree, Server Components, Hooks y gestión eficiente del estado.
2. Core Web Vitals & Rendimiento Web: Métricas LCP (Largest Contentful Paint), INP (Interaction to Next Paint), CLS (Cumulative Layout Shift) y técnicas de optimización visual.
3. Patrones PWA & Offline-First: Service Workers, Cache Storage API, Background Sync y persistencia local sin degradación.
4. Accesibilidad (WCAG 2.2 AA) & UI/UX: Ergonomía cognitiva, sistemas de diseño dinámicos con tokens OKLCH y diseño responsivo.
5. Checklist Auditoría Web & Ejercicios de Diagnóstico.
```

---

### ☁️ Prompt 10: Cuaderno de Infraestructura Cloud-Native, GCP & Serverless

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Principal Cloud Architect (GCP Champion). Extrayendo EXCLUSIVAMENTE el Módulo 5 de infraestructura Cloud-Native y GCP del Libro Máster PDF y del README principal, genera un Cuaderno Formativo sobre Infraestructura Cloud-Native y Plataformas GCP.

El Cuaderno abarcará:
1. Contenedores Linux & Runtime: Namespaces, Cgroups v2, aislación de procesos y arquitectura interna de Kubernetes (etcd, kube-apiserver, Scheduler).
2. Serverless Masivo en GCP: Cloud Run, Cloud Tasks, Firestore, triggers asíncronos y optimización FinOps para escala $0.015 USD/MAU/mes.
3. Infraestructura como Código (IaC): Terraform declarativo, GitOps con ArgoCD y gestión inmutable del estado.
4. Casos de Estudio de Despliegue: Arquitectura multirregión con alta disponibilidad y escalado automático a cero.
5. Preguntas de Examen de Certificación Cloud Architect.
```

---

### 🛡️ Prompt 11: Cuaderno de SRE, Resiliencia & Telemetría OpenTelemetry

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Google SRE Site Reliability Director. Extrayendo EXCLUSIVAMENTE el Módulo 6 de SRE, resiliencia y telemetría del Libro Máster PDF y del README principal, redacta un Cuaderno Formativo sobre SRE, Observabilidad y Resiliencia de Sistemas.

El Cuaderno cubrirá:
1. Métricas de Fiabilidad: Definiendo SLIs (Service Level Indicators), SLOs (Service Level Objectives) y gestión estratégica de Error Budgets.
2. Observabilidad de Extremo a Extremo: Tracing distribuido con OpenTelemetry (OTEL), métricas Prometheus y agregación estructurada de logs.
3. Patrones de Resiliencia: Circuit Breakers predictivos, Rate Limiting, Shedding de carga y aislamiento por Mamparas (Bulkheading).
4. Gestión de Incidentes & Postmortems: Cultura blameless, análisis de causa raíz y planes de mitigación de emergencia.
5. Ejercicios Prácticos de Simulación de Incidencias & Evaluación SRE.
```

---

### 🗄️ Prompt 12: Cuaderno de Bases de Datos NoSQL, OLAP & Multi-Tenancy Segura

**📄 Documentos de Referencia Exclusivos:**
- `@ [REFERENCIA PRINCIPAL] README.md`
- `@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf`

```text
RESTRICCIÓN DE CONTEXTO: Utiliza ÚNICAMENTE el README.md principal (@ [REFERENCIA PRINCIPAL] README.md) y el Libro Máster (@ [LIBRO MÁSTER PDF] CORPORATE_ARCHITECTURE_MASTER_BOOK.pdf) como fuentes de información. Ignora cualquier otro documento individual o módulo no listado.

Actúa como un Chief Data Architect experto en almacenamiento distribuido y seguridad multi-inquilino. Extrayendo EXCLUSIVAMENTE el Módulo 7 y la sección Fintech del Libro Máster PDF y del README principal, construye un Cuaderno Formativo sobre Bases de Datos y Multi-Tenancy.

El Cuaderno debe estructurarse en:
1. Almacenamiento Columnar OLAP (BigQuery): Internals de Dremel, partitioning por fecha, clustering por `tenant_id` y ejecuciones costo-eficientes con BQML.
2. Bases de Datos Documentales NoSQL (Firestore): Arquitectura de índices, alta concurrencia en lecturas/escrituras y diseño de colecciones atómicas.
3. Aislamiento Multi-Tenant & RLS: Firestore Security Rules, Row-Level Security en SQL y prevención estricta de fugas de datos entre inquilinos.
4. Integración Fintech: Idempotencia transaccional con Stripe Connect, patron Saga y cuentas de custodia (Escrow).
5. Examen Teórico-Práctico de Seguridad e Integridad de Datos.
```


---
## 🧠 Ejercicio Práctico: El Método Feynman

Para garantizar una asimilación profunda de los conceptos presentados en este módulo, aplica el **Método Feynman**:

> **Instrucción:** Explica los conceptos centrales de este módulo como si tu audiencia fuera un estudiante brillante de 12 años que no ha visto nunca este tema. Si no puedes hacerlo con lenguaje sencillo, analogías claras y sin jerga técnica, significa que aún no lo entiendes lo suficientemente bien.

*Inténtalo tú mismo:* Toma el concepto más complejo de este módulo, escríbelo en un papel en blanco y redáctalo usando únicamente términos cotidianos.
