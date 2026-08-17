# core-zkp-privacy - Módulo Criptográfico de Privacidad Zero-Knowledge

Módulo algorítmico puro de criptografía Zero-Knowledge (ZKP) para privacidad diferencial, compromisos de Pedersen homomórficos y pruebas de rango no interactivas Fiat-Shamir en el ecosistema corporativo.

## Arquitectura Criptográfica

El motor [`ZkpProofEngine`](file:///home/jaruiz/Desarrollo/core/core-zkp-privacy/src/main/java/com/corp/zkp/privacy/ZkpProofEngine.java) implementa esquemas criptográficos sobre el campo primo estándar secp256k1:

1. **Compromiso Homomórfico de Pedersen**:
   \[
   C(v, r) = g^v \cdot h^r \pmod p
   \]
   donde \(g\) y \(h\) son generadores del grupo con logaritmo discreto desconocido entre sí (\(\log_g(h)\) desconocido).
   - **Propiedad Homomórfica Aditiva**:
     \[
     C(v_1, r_1) \cdot C(v_2, r_2) = g^{v_1 + v_2} \cdot h^{r_1 + r_2} \pmod p = C(v_1 + v_2, r_1 + r_2)
     \]
2. **Pruebas de Rango No Interactivas (Fiat-Shamir)**:
   - Descomposición binaria del valor secreto \(v = \sum_{i=0}^{N-1} b_i \cdot 2^i\) con \(b_i \in \{0, 1\}\).
   - Desafío no interactivo generado vía hash criptográfico SHA-256 \(c = H(C \parallel \dots \parallel C_i)\).
   - Verificación en \(O(1)\) respecto a la complejidad de red sin divulgación de secretos.

## Modelos de Dominio (Records Inmutables Java 25)

- [`PedersenCommitment`](file:///home/jaruiz/Desarrollo/core/core-zkp-privacy/src/main/java/com/corp/zkp/privacy/domain/PedersenCommitment.java): Registro inmutable que almacena el valor oculto, el factor de ceguera (*blinding factor*) y el punto de compromiso en la curva/grupo.
- [`ZkpRangeProof`](file:///home/jaruiz/Desarrollo/core/core-zkp-privacy/src/main/java/com/corp/zkp/privacy/domain/ZkpRangeProof.java): Estructura de prueba con compromisos por bit, desafío Fiat-Shamir y respuestas de Schnorr.
- [`ZkpVerificationResult`](file:///home/jaruiz/Desarrollo/core/core-zkp-privacy/src/main/java/com/corp/zkp/privacy/domain/ZkpVerificationResult.java): Resultado booleano con traza criptográfica y latencia de verificación.

## Rendimiento y Concurrencia

- **Cero Carrier Thread Pinning**: Sincronización protegida mediante `ReentrantLock` y `SecureRandom` sin bloques `synchronized`.
- **Compatibilidad AOT / Leyden**: Cero reflexión en la capa de cálculo numérico.

## Referencias Académicas

- Pedersen, T. P. (1991). *Non-Interactive and Information-Theoretic Secure Verifiable Secret Sharing*. CRYPTO '91.
- Fiat, A., & Shamir, A. (1986). *How to Prove Yourself: Practical Solutions to Identification and Signature Problems*. CRYPTO '86.
- Bünz, B., et al. (2018). *Bulletproofs: Short Proofs for Confidential Transactions and More*. IEEE S&P.
