# ADR-019: Criptografía Zero-Knowledge (ZKP), Compromisos de Pedersen y Transformada Fiat-Shamir

## Estado
**Aceptado**

## Contexto
Los verticales empresariales del ecosistema (`ProyectoSalud`, `ProyectoB2G`, `ProyectoTokenRWA`) requieren verificar solvencia financiera, cotas regulatorias y elegibilidad de ensayos clínicos sin revelar datos personales sensibles (Zero-PII) ni violar regulaciones internacionales (GDPR, HIPAA, FDA 21 CFR Part 11).

## Decisión
Implementar un módulo algorítmico puro [`core-zkp-privacy`](file:///home/jaruiz/Desarrollo/core/core-zkp-privacy) en Java 25:
1. **Compromisos de Pedersen Homomórficos**:
   \[
   C(v, r) = g^v \cdot h^r \pmod p
   \]
   Garantiza *information-theoretic hiding* y *computational binding* sobre el campo primo secp256k1.
2. **Propiedad Homomórfica Aditiva**:
   Permite agregar balances y cotas en tiempo \(O(1)\) sin descifrado:
   \[
   C(v_1, r_1) \cdot C(v_2, r_2) = C(v_1 + v_2, r_1 + r_2 \pmod q)
   \]
3. **Pruebas de Rango No Interactivas (Fiat-Shamir)**:
   Verificación no interactiva con desafío derivado mediante hash criptográfico SHA-256 \(O(1)\) sin sobrecarga de red.
4. **Cero Carrier Thread Pinning**:
   Sincronización interna protegida con `ReentrantLock` y `SecureRandom` para máxima concurrencia en Virtual Threads.

## Consecuencias
- **Positivas**:
  - Auditoría criptográfica determinista con latencia \(< 1\text{ ms}\).
  - Privacidad formal demostrable para telemetría clínica y transacciones confidenciales.
- **Negativas**:
  - Cómputo modular de precisión arbitraria (`BigInteger`) en CPU, mitigado mediante pools in-memory y reutilización de generadores constantes.
