# core-govtech-ledger — Motor Criptográfico de Merkle Trees & Libro Mayor Inmutable

Módulo algorítmico central en **Java 25 LTS puro** para la construcción, sellado y verificación criptográfica de libros mayores basados en Árboles de Merkle binarios balanceados con pruebas de inclusión en \(O(\log N)\).

---

## 1. Fundamentos Teóricos y Arquitectura

El motor implementa las garantías de inmutabilidad y transparencia requeridas por el estándar **GovTech Ledger** (Universidad Privada del Ecosistema, Facultad VI: Criptografía y Consenso).

### Ecuaciones y Algoritmos Gobernantes

1. **Función de Resumen Criptográfico:**
   \[ h(x) = \text{SHA-256}(x) \]

2. **Árbol Binario de Merkle:**
   Dado un conjunto de \(N\) transacciones \(\{T_1, T_2, \dots, T_N\}\), las hojas se calculan como:
   \[ L_i = h(T_i) \]
   Los nodos internos de nivel \(k\) se calculan combinando pares de nodos hijos:
   \[ N_{k, j} = h(N_{k-1, 2j} \parallel N_{k-1, 2j+1}) \]
   Si el número de nodos en un nivel es impar, el último nodo se duplica (\(N_{k-1, 2j} \parallel N_{k-1, 2j}\)) garantizando un árbol balanceado estricto.

3. **Pruebas de Inclusión (Merkle Proofs):**
   Para cualquier transacción \(T_i\), se genera una secuencia de \(\lceil \log_2 N \rceil\) hashes hermanos:
   \[ \Pi(T_i) = \{ (H_1, \text{dir}_1), (H_2, \text{dir}_2), \dots, (H_m, \text{dir}_m) \} \]
   La verificación recalcula el hash raíz y verifica la igualdad determinista:
   \[ \text{Verify}(\Pi(T_i), T_i, \text{RootHash}) \in \{\text{true}, \text{false}\} \]

4. **Encadenamiento Temporal de Bloques (Block Chaining):**
   \[ \text{BlockHash}_b = h(\text{BlockIndex}_b \parallel \text{PrevBlockHash}_{b-1} \parallel \text{MerkleRoot}_b \parallel \text{Timestamp}) \]

---

## 2. Componentes Principales

- **`GovtechMerkleLedgerEngine`**:
  - `sealCurrentBlock()`: Sella las transacciones pendientes en un bloque inmutable con su correspondiente Merkle Root.
  - `generateMerkleProof(transactionId)`: Genera la prueba de inclusión \(O(\log N)\) para una transacción dada.
  - `verifyMerkleProof(proof)`: Valida la autenticidad matemática de la prueba sin necesidad de descargar el bloque completo.
  - `verifyChainIntegrity()`: Recorre y verifica el encadenamiento de todos los bloques desde el Bloque Génesis en \(O(B \cdot N)\).
- **Inmutabilidad y Concurrencia:**
  - Registros de datos definidos mediante **Java 25 Records** (`MerkleBlock`, `LedgerTransaction`, `MerkleProofStep`, `MerkleProof`).
  - Exclusión mutua gestionada con `java.util.concurrent.locks.ReentrantLock` para compatibilidad nativa con **Virtual Threads (Project Loom)**, eliminando el riesgo de *Carrier Thread Pinning*.

---

## 3. Pruebas y Certificación de Calidad

- **Zero-Mockito Estricto:** 100% de las pruebas unitarias y de integración son herméticas, sin mocks ni dependencias externas.
- **Concurrencia Masiva:** Suite de pruebas con 100 Virtual Threads concurrentes sellando transacciones.
- **Comando de Ejecución:**
  ```bash
  mvn clean test
  ```