# Notion Project Dossier: ProyectoDefensa (Ciberdefensa Soberana y Detección de Amenazas)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `vertical-proyecto-defensa-v1`
* **Líder Técnico / Arquitecto**: `@Zero-Trust-Security-Auditor` & `@Unified-Twin-Architect`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `CRÍTICA (P0)`
* **Región de Despliegue**: Entorno Aislado / Soberano GCP (`europe-southwest1`)
* **Coste FinOps PRO**: **`$35,00 USD/mes`**
* **Stack Tecnológico**: Java 25 LTS, Spring Boot 4.0, Zero-Trust BeyondCorp, BigQuery ML K-Means Anomalies, SLSA L4 Provenance.

---

## 2. Visión General y Objetivos de Negocio
Detección en tiempo real de anomalías de red, integridad de la cadena de suministro de software (SBOM / Cosign) y protección perimetral sin confianza implícita (*Zero-Trust*).
* **KPI Tiempo de Detección de Intrusión (MTTD)**: **`< 250 ms`**.
* **KPI Inmutabilidad de Atestaciones**: 100% firmado bajo Sigstore / Cosign.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[DEF-101]** Detección de anomalías en paquetes de red mediante K-Means in-situ en BigQuery ML.
- [x] **[DEF-102]** Verificación de firmas Sigstore en cada etapa del pipeline CI/CD.
- [x] **[DEF-103]** Aislamiento celular por microsegmentación de red y VPC Service Controls.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-001](file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md), [ADR-004](file:///home/jaruiz/Desarrollo/docs/adr/adr-004-zero-trust-beyondcorp-slsa.md), [ADR-008](file:///home/jaruiz/Desarrollo/docs/adr/adr-008-slsa-l3-sigstore-provenance.md), [ADR-011](file:///home/jaruiz/Desarrollo/docs/adr/adr-011-lmax-ringbuffer-bi-engine-and-adaptive-enkf.md).
