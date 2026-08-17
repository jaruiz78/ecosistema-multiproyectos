# ProyectoVPP — Centrales Eléctricas Virtuales (VPP), DERs & Agregación BESS

Módulo empresarial para la gestión, agregación y despacho óptimo de Centrales Eléctricas Virtuales (Virtual Power Plants) y Recursos Energéticos Distribuidos (DERs) con almacenamiento en baterías (BESS).

---

## 1. Arquitectura Hexagonal y Stack Técnico
- **Lenguaje:** Java 25 LTS (`record` inmutables, Virtual Threads Loom, Zero-Mockito).
- **Framework Base:** Spring Boot 4.1.0 (hereda de `corp-spring-boot-starter-parent`).
- **Resiliencia:** Cálculo analítico de rampa en \(O(N)\) y reserva rodante (Spinning Reserve).

## 2. Agregados de Dominio y Modelos
1. **`VPP`**: Agregado raíz para la orquestación del portafolio de energía distribuida.
2. **`BatteryEnergyStorageUnit`**: Unidad de almacenamiento BESS con química (LFP, NMC, Sodio), capacidad en kWh y State of Charge (\(0\% \le \text{SoC} \le 100\%\)).

## 3. Servicios de Negocio
- **`ClusterCapacityAggregator`**:
  - Agregación instantánea de capacidad de descarga y carga neta en \(O(N)\) respetando márgenes de seguridad (\(10\% \le \text{SoC} \le 95\%\)).
  - Estimación de potencia de rampa (\(\text{kW}\)) y reserva rodante para regulación de frecuencia de red (20% del pool).
  - Cálculo ponderado del estado de carga medio del cluster.

## 4. Pruebas y Certificación
- **100% Zero-Mockito**: Suites herméticas de prueba sin dependencias de infraestructura.
- **Ejecución:** `mvn clean test`