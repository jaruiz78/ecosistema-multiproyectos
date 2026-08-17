# ProyectoTokenRWA — Tokenización de Activos del Mundo Real (RWA) & Escrow Sagas

Módulo empresarial de tokenización fraccionada de activos físicos (infraestructuras energéticas, inmobiliario industrial y derechos de agua) con conciliación contable sin pérdidas y liquidación multi-fase Escrow.

---

## 1. Arquitectura Hexagonal y Stack Técnico
- **Lenguaje:** Java 25 LTS (`record` inmutables, Virtual Threads Loom, Zero-Mockito).
- **Framework Base:** Spring Boot 4.1.0 (hereda de `corp-spring-boot-starter-parent`).
- **Seguridad Financiera:** Aritmética de precisión con `BigDecimal` y redondeo Banker's Rounding (IEEE 754-2008).

## 2. Agregados de Dominio y Modelos
1. **`TokenRWA`**: Agregado raíz para activos tokenizados y emisión de participaciones.
2. **`EscrowAssetVault`**: Bóveda de custodia escrow con control de colateral bloqueado, estado de liberación y firma notarial.

## 3. Servicios de Negocio
- **`TokenFractionEngine`**:
  - Fraccionamiento determinista de activos y distribución de dividendos en \(O(N)\).
  - Conciliación de residuos contables:
    \[ \sum_{i=1}^N v_i + r = V_{\text{total}}, \quad r < 10^{-6} \]

## 4. Pruebas y Certificación
- **100% Zero-Mockito**: Suites herméticas de prueba sin dependencias externas.
- **Ejecución:** `mvn clean test`
