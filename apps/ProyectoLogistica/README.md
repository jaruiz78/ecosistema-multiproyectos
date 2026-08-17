# ProyectoLogistica — Optimización VRP Estocástica, Flotas Autónomas & Logística H3

Módulo empresarial de logística verde, ruteo vehicular con ventanas de tiempo (VRPTW) y optimización de emisiones bajo mallas geoespaciales Uber H3.

---

## 1. Arquitectura Hexagonal y Stack Técnico
- **Lenguaje:** Java 25 LTS (`record` inmutables, Virtual Threads Loom, Zero-Mockito).
- **Framework Base:** Spring Boot 4.1.0 (hereda de `corp-spring-boot-starter-parent`).
- **Resiliencia:** Heurística de inserción temporal y cálculo de coste marginal en \(O(N \log N)\).

## 2. Agregados de Dominio y Modelos
1. **`Logistica`**: Agregado raíz para la orquestación del operador logístico.
2. **`AutonomousFleetRoute`**: Rutas de vehículos autónomos con control de carga útil en kg, celdas H3 y estado de tráfico.

## 3. Servicios de Negocio
- **`StochasticVrpEngine`**:
  - Función de coste multi-criterio:
    \[ \min \sum_{i,j} c_{ij} x_{ij} + \sum_i \alpha \max(0, a_i - l_i) + \sum_{i,j} e_{ij} x_{ij} \]
  - Modelado de ventanas de tiempo, penalizaciones por demora en entrega (\(1.50\text{ EUR/min}\)) y cálculo de huella de carbono (\(120\text{ g CO}_2/\text{km}\)).

## 4. Pruebas y Certificación
- **100% Zero-Mockito**: Pruebas unitarias de dominio y de optimización de rutas herméticas in-memory.
- **Ejecución:** `mvn clean test`
