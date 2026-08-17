# 🏛️ Dossier Notion: core-zkp-privacy

## 1. Identificación del Módulo
* **Nombre**: `core-zkp-privacy`
* **Tipo**: Motor Criptográfico Puro de Privacidad y Pruebas de Conocimiento Cero
* **Lenguaje & Runtime**: Java 25 LTS, Java Records, BigInteger seguro
* **Complejidad Asintótica**: \(O(1)\) por verificación de compromiso homomórfico y range proof

---

## 2. Diagrama de Arquitectura y Flujo Criptográfico

```mermaid
flowchart TD
    Secret["Valor Secreto v (ej. Saldo, Edad, pH)"] --> Pedersen["Pedersen Commitment: C = g^v * h^r mod p"]
    Pedersen --> Range["Fiat-Shamir Non-Interactive Range Proof"]
    Range --> Verifier["ZkpProofEngine (Verificador O(1))"]
    Verifier --> Result{"¿Prueba Válida?"}
    Result -- Sí --> Accept["Transacción Aprobada sin revelar 'v'"]
    Result -- No --> Reject["Rechazo Criptográfico Inmediato"]
    Accept --> Twin["tensor_gnn_core.py (Cluster 10: ZKP Privacy)"]
```

---

## 3. Estado de Calidad y Pruebas
* **Zero-Mockito TDD**: 6 tests unitarios JUnit 5 (100% verdes).
* **Dependencias Externas**: 0 (Java Puro).
* **Universidad Privada**: Facultad X (Criptografía y Privacidad) y Facultad I (Ingeniería de Software).
