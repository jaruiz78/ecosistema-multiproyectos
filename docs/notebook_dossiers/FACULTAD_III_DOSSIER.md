# DOSSIER ACADÉMICO: FACULTAD_III - Runtime JVM, Virtual Threads (Loom) & AOT Leyden CDS
**Cátedra de Referencia:** OpenJDK HotSpot / ETH Zurich
**Ecosistema:** Google Antigravity & Multi-Proyecto Corporativo

---

## 1. Fundamentos Teóricos y Teoremas Centrales
1. **Modelo de Virtual Threads de Java 25 y Ausencia de Carrier Thread Pinning**
2. **Entrenamiento Leyden CDS (.jsa) para Cold-Starts < 80ms en Serverless**
3. **Project Panama (Foreign Function & Memory API) y Off-Heap Allocation**
4. **ReentrantLock vs Synchronized en Concurrencia de Millones de Hilos**

---

## 2. Palabras Clave y Ontología Semántica
Java 25, Project Loom, Project Leyden, CDS, AOT, Cloud Run, GraalVM

---

## 3. Directrices de Implementación en Código
- **Lenguajes y Runtimes:** Alineados estrictamente con Java 25 (LTS), Go 1.26, Python 3.12 y Dart/Flutter.
- **Rigor Asintótico:** Preferencia obligatoria por algoritmos $O(1)$ o $O(N \log N)$.
- **Cero Dependencias Ociosas:** Toda dependencia añadida debe cumplir el Filtro Tripartito de Decisión.

---

## 4. Preguntas Socráticas para NotebookLM & Auto-Evaluación
1. ¿De qué manera esta facultad previene regresiones arquitectónicas en el sistema?
2. ¿Cómo se demuestra formalmente que las invariantes se mantienen bajo carga extrema?
3. ¿Cuál es el impacto directo de esta facultad en la métrica FinOps $< 0.015\text{ USD/MAU/mes}$?
