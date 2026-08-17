# ProyectoB2G — GovTech, Contratación Pública & Auditoría Criptográfica

Módulo empresarial para la gestión, predicción espaciotemporal (ST-GNN) y sellado criptográfico inmutable de licitaciones de contratos del sector público.

---

## 1. Arquitectura Hexagonal y Stack Técnico
- **Lenguaje:** Java 25 LTS (`record` inmutables, Virtual Threads Loom, Zero-Mockito).
- **Framework Base:** Spring Boot 4.1.0 (hereda de `corp-spring-boot-starter-parent`).
- **Seguridad e Integridad:** Libro mayor inmutable SHA-256 con encadenamiento de hash por licitación.

## 2. Agregados de Dominio y Modelos
1. **`B2G`**: Agregado raíz para operaciones y flujos GovTech.
2. **`PublicProcurementContract`**: Contrato público con NIF de licitador, presupuesto de adjudicación y estado de cumplimiento.

## 3. Servicios de Negocio
- **`PublicTenderAuditLedgerService`**:
  - Sellado inmutable de ofertas económicas y pliegos técnicos en \(O(1)\).
  - Trazabilidad y encadenamiento criptográfico:
    \[ h_k = \text{SHA256}(c_k \parallel \text{NIF} \parallel \text{EUR} \parallel \text{payloadHash} \parallel h_{k-1} \parallel t) \]
- **`StGnnPredictor`**:
  - Inferencia espaciotemporal de demanda de servicios públicos.

## 4. Pruebas y Certificación
- **100% Zero-Mockito**: Pruebas unitarias herméticas sin efectos colaterales de red o I/O.
- **Ejecución:** `mvn clean test`
