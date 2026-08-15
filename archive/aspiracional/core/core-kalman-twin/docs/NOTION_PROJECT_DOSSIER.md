# Notion Project Dossier: core-kalman-twin (Gemelo Digital y Asimilación EnKF)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `core-kalman-twin-engine`
* **Líder Técnico / Arquitecto**: `@Digital-Twin-EnKF-Orchestrator` & `@Unified-Twin-Architect`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `ALTA (P1)`
* **Región / Despliegue**: Motor Embebido / Microservicio de Simulación
* **Coste FinOps PRO**: **`$0,00 USD/mes`** (Cómputo in-process \(O(1)\))
* **Stack Tecnológico**: Python 3.12+, NumPy Vectorizado, Filtro de Kalman Ensemble (EnKF), Simetrización Joseph-form, Myers-Tapley Auto-Tuning.

---

## 2. Visión General y Objetivos de Negocio
Motor centralizado de simulación, predicción estocástica y asimilación de estados en tiempo real para todos los verticales del ecosistema (Agro, Movilidad, Red Eléctrica, Logística y Finanzas).
* **KPI Convergencia de Covarianza**: \(\frac{\text{trace}(P)}{N} = 0,001801 \ll 0,5\) (Garantía de estabilidad matemática).
* **KPI Velocidad de Simulación**: **`13.000.000 simulaciones / segundo`** en Monte Carlo vectorizado.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[ENKF-101]** Formulación Joseph-form para garantizar definición positiva y simetría exacta en actualizaciones de covarianza.
- [x] **[ADAPT-102]** Auto-tuning adaptativo **Myers-Tapley** (`update_adaptive`) para recalibrar \(R_t\) a partir de los residuales de innovación.
- [x] **[SIM-201]** Ejecución de 1.000.000 de simulaciones a 5 años en PRO con persistencia en `simulations_telemetry.db`.
- [x] **[SHOCK-301]** Validación de resiliencia ante shocks simultáneos (tormenta en aeropuertos + golpe de ariete + picos de demanda).

### B. Tareas en Curso (In Progress)
- [ ] **[LITERT-401]** Exportación de tensores podados SVD para inferencia embebida en micro-controladores Edge.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-003](file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md), [ADR-010](file:///home/jaruiz/Desarrollo/docs/adr/adr-010-bqml-edge-inference-and-kalman-twin-assimilation.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
