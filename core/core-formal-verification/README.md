# core-formal-verification - Módulo Algorítmico Puro de Verificación Formal

Módulo algorítmico de verificación formal y cálculo de invariantes inductivos basado en Lógica de Hoare para contratos de estado y orquestación de sistemas distribuidos en el ecosistema corporativo.

## Fundamentos Teóricos

El motor [`HoareInvariantVerifier`](file:///home/jaruiz/Desarrollo/core/core-formal-verification/src/main/java/com/corp/formal/verification/HoareInvariantVerifier.java) implementa el marco axiomático de Hoare:

1. **Terna de Hoare**:
   \[
   \{P\}\; C \;\{Q\}
   \]
   Donde:
   - \(P\): Precondición sobre el estado previo \(s\).
   - \(C\): Comando o transición funcional de estado \(s \to s'\).
   - \(Q\): Poscondición relacional sobre el par \((s, s')\).

2. **Invariante Inductivo de Bucle**:
   \[
   \frac{P \implies Inv, \quad \{Inv \land B\}\; C \;\{Inv\}, \quad (Inv \land \neg B) \implies Q}{\{P\}\; \text{while } B \text{ do } C \;\{Q\}}
   \]

3. **Certificados Criptográficos Inmutables**:
   - Cada certificación emite un [`VerificationCertificate`](file:///home/jaruiz/Desarrollo/core/core-formal-verification/src/main/java/com/corp/formal/verification/domain/VerificationCertificate.java) con hash SHA-256 de auditoría y traza de tiempo en nanosegundos.

## Modelos de Dominio (Records Inmutables Java 25)

- [`HoareTriple`](file:///home/jaruiz/Desarrollo/core/core-formal-verification/src/main/java/com/corp/formal/verification/domain/HoareTriple.java): Terna funcional tipada con predicados de precondición, comando y poscondición.
- [`StateInvariant`](file:///home/jaruiz/Desarrollo/core/core-formal-verification/src/main/java/com/corp/formal/verification/domain/StateInvariant.java): Invariante formal con nombre, justificación y predicado determinista.
- [`VerificationCertificate`](file:///home/jaruiz/Desarrollo/core/core-formal-verification/src/main/java/com/corp/formal/verification/domain/VerificationCertificate.java): Certificado inmutable con veredicto, invariantes chequeados y digest SHA-256.

## Rendimiento y Concurrencia

- **Cero Carrier Thread Pinning**: Sincronización mediante `ReentrantLock` sin bloques `synchronized`.
- **Compatibilidad AOT / Leyden**: Modelos 100% inmutables Java 25 Records sin dependencias externas ni reflexión.

## Referencias Académicas

- Hoare, C. A. R. (1969). *An Axiomatic Basis for Computer Programming*. Communications of the ACM.
- Lamport, L. (2002). *Specifying Systems: The TLA+ Language and Tools for Hardware and Software Engineers*. Addison-Wesley.
- Dijkstra, E. W. (1976). *A Discipline of Programming*. Prentice-Hall.
