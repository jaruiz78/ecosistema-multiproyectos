# Notion Project Dossier: core-govtech-ledger (Ledger Criptográfico Inmutable)

## 1. Propiedades y Metadatos de Notion
* **ID Notion Wiki**: `core-govtech-ledger-engine`
* **Líder Técnico / Arquitecto**: `@Zero-Trust-Security-Auditor` & `@Stripe-Fintech-Engineer`
* **Estado en Kanban**: `EN PRODUCCIÓN / OPTIMIZADO`
* **Prioridad**: `ALTA (P1)`
* **Región / Despliegue**: Librería Embebida / Validador Criptográfico
* **Coste FinOps PRO**: **`$0,00 USD/mes`**
* **Stack Tecnológico**: Java 25 Records, SHA-256 Merkle Trees, Ed25519 Signatures, Sigstore / Cosign SLSA L3.

---

## 2. Visión General y Objetivos de Negocio
Registro inmutable de auditoría, trazabilidad de transacciones de tokens RWA, actas de gobierno B2G y certificados de custodia.
* **KPI Verificación Criptográfica**: **`< 10 µs`** por bloque Merkle.
* **KPI Inmutabilidad**: 100% de transacciones firmadas y auditadas sin posibilidad de modificación retrospectiva.

---

## 3. Tablero Kanban de Tareas y Estado

### A. Tareas Completadas (Done / Released)
- [x] **[LEDGER-101]** Árboles de Merkle binarios balanceados para verificación de inclusión \(O(\log N)\).
- [x] **[LEDGER-102]** Atestación SLSA L3 automatizada y firmado con claves efímeras Sigstore.
- [x] **[LEDGER-103]** Exportación de balances auditables para `ProyectoTokenRWA` y `ProyectoB2G`.

---

## 4. Referencias y ADRs
* **ADRs Vinculados**: [ADR-004](file:///home/jaruiz/Desarrollo/docs/adr/adr-004-zero-trust-beyondcorp-slsa.md), [ADR-008](file:///home/jaruiz/Desarrollo/docs/adr/adr-008-slsa-l3-sigstore-provenance.md).
