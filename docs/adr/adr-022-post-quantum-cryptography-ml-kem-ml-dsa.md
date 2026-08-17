# ADR-022: Criptografía Post-Cuántica (PQC) NIST ML-KEM y ML-DSA

## Estado
Aprobado - Agosto 2026

## Contexto
Con la evolución de la computación cuántica y los estándares del NIST (FIPS 203 ML-KEM / Crystals-Kyber y FIPS 204 ML-DSA / Crystals-Dilithium), el ecosistema corporativo requiere protección de secreto perfecto hacia el futuro (Forward Secrecy Post-Quantum) para las comunicaciones inter-servicios, tokens de autenticación y transacciones de activos reales (RWA) y defensa.

## Decisión
1. Incorporar el starter `corp-crypto-postquantum-starter` dentro del BOM corporativo `corp-spring-boot-starter`.
2. Implementar `PostQuantumSecurityManager` con soporte para encapsulación de claves ML-KEM-768 y firmas digitales ML-DSA-65.
3. Garantizar modo híbrido (Ed25519/ECDSA + PQC) para compatibilidad hacia atrás y latencias de serialización \(< 2\text{ ms}\) en Java 25.

## Consecuencias
- **Positivas**: Inmunidad cuántica para datos clasificados, cumplimiento con directivas de defensa y banca internacional.
- **Negativas**: Aumento en el tamaño de las firmas y claves públicas respecto a curvas elípticas clásicas (\(\approx 1\text{ a } 2.5\text{ KB}\)).
