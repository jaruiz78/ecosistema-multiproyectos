# 🏛️ Dossier Notion: core-formal-verification

## 1. Identificación del Módulo
* **Nombre**: `core-formal-verification`
* **Tipo**: Motor Algorítmico Puro de Verificación Formal y Lógica de Hoare
* **Lenguaje & Runtime**: Java 25 LTS, Java Records
* **Complejidad Asintótica**: \(O(1)\) por comprobación inductiva de invariantes de estado

---

## 2. Diagrama de Arquitectura y Flujo de Verificación Formal

```mermaid
flowchart TD
    Pre["Precondición P(s)"] --> State["Transición de Estado / Acción s -> s'"]
    State --> Post["Postcondición Q(s')"]
    Post --> Invariant["HoareInvariantVerifier: P(s) { Acción } Q(s') & I(s')"]
    Invariant --> Check{"¿Invariante Preservado?"}
    Check -- Sí --> Digest["Generación de Digest Criptográfico SHA-256"]
    Check -- No --> SafetyViolation["Fallo de Seguridad / Excepción Inmediata"]
    Digest --> Audit["Trazabilidad Inmutable en Ledger"]
```

---

## 3. Estado de Calidad y Pruebas
* **Zero-Mockito TDD**: 6 tests unitarios JUnit 5 (100% verdes).
* **Dependencias Externas**: 0 (Java Puro).
* **Universidad Privada**: Facultad IX (Verificación Formal y Demostración de Teoremas).
