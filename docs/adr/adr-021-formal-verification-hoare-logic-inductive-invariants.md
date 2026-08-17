# ADR-021: Verificación Formal de Contratos basada en Lógica de Hoare e Invariantes Inductivos

## Estado
**Aceptado**

## Contexto
En sistemas ciberfísicos críticos, transacciones financieras multi-tenant y orquestación de drones, las pruebas estocásticas no son suficientes para garantizar la ausencia total de errores de seguridad (*safety*) y progreso (*liveness*). Se requiere certificación matemática determinista pre-ejecución.

## Decisión
Implementar [`core-formal-verification`](file:///home/jaruiz/Desarrollo/core/core-formal-verification) como biblioteca pura de verificación formal en Java 25:
1. **Ternas de Hoare**:
   Verificación rigurosa de contratos \(\{P\}\; C \;\{Q\}\) con precondiciones, funciones de transición y poscondiciones relacionales.
2. **Inducción de Bucles e Invariantes**:
   Validación de invariantes inductivos \(P \implies Inv\), \(\{Inv \land B\}\; C \;\{Inv\}\), \(\{Inv \land \neg B\} \implies Q\).
3. **Certificados Criptográficos Inmutables**:
   Generación de tokens `VerificationCertificate` con hash SHA-256 inyectados en la telemetría de auditoría de `simulations_telemetry.db`.
4. **Pureza Hexagonal**:
   Cero dependencias de librerías externas o reflexión.

## Consecuencias
- **Positivas**:
  - Demostrabilidad formal de propiedades de conservación (masa, energía, balance financiero) en tiempo de ejecución.
  - Trazabilidad auditable para organismos reguladores y el Consilium Romano 3.0.
- **Negativas**:
  - Requiere formalizar explícitamente los invariantes algebraicos de cada dominio de negocio.
