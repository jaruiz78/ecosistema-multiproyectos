# DOSSIER ACADÉMICO: FACULTAD_II - Sistemas Distribuidos, Consenso & Verificación TLA+
**Cátedra de Referencia:** MIT 6.5840 / UC Berkeley RISELab
**Ecosistema:** Google Antigravity & Multi-Proyecto Corporativo

---

## 1. Fundamentos Teóricos y Teoremas Centrales
1. **Teorema FLP (Fischer-Lynch-Paterson) sobre la Imposibilidad de Consenso Asíncrono**
2. **Relojes Lógicos y Causalidad de Lamport (Happened-Before Relation)**
3. **Consenso Raft & Paxos: Seguridad de Elección de Líder y Log Matching**
4. **Teorema PACELC (Extensión de CAP en Latencia vs Consistencia)**

---

## 2. Palabras Clave y Ontología Semántica
Raft, Paxos, Lamport Clocks, TLA+, Consenso Bizantino, Idempotencia

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
