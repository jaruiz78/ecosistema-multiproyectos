# ProyectoEnergia — Redes Eléctricas Inteligentes, AC-OPF & Despacho Energético

Módulo empresarial para la optimización de flujos de carga en redes de transporte y distribución eléctrica (AC Optimal Power Flow), modelado de subestaciones y despacho económico.

---

## 1. Arquitectura Hexagonal y Stack Técnico
- **Lenguaje:** Java 25 LTS (`record` inmutables, Virtual Threads Loom, Zero-Mockito).
- **Framework Base:** Spring Boot 4.1.0 (hereda de `corp-spring-boot-starter-parent`).
- **Física de Red:** Formulación no lineal de inyecciones de potencia activa (\(P\)) y reactiva (\(Q\)).

## 2. Agregados de Dominio y Modelos
1. **`Energia`**: Agregado raíz para operaciones de despacho y mercado eléctrico.
2. **`GridSubstationNode`**: Subestación eléctrica con tensión nominal en kV (\(V_{\text{kv}} > 0\)), frecuencia de red en Hz y operador de sistema.

## 3. Servicios de Negocio
- **`ACOptimalPowerFlowSolver`**:
  - Ecuaciones de balance de potencias en barras:
    \[ P_i = V_i \sum_{j=1}^N V_j (G_{ij} \cos \theta_{ij} + B_{ij} \sin \theta_{ij}) \]
    \[ Q_i = V_i \sum_{j=1}^N V_j (G_{ij} \sin \theta_{ij} - B_{ij} \cos \theta_{ij}) \]
  - Verificación de límites térmicos (\(\text{MVA}\)), caídas de tensión en \(pu\) y pérdidas Joule.
- **`LinearOpfDispatcher`**:
  - Despacho linealizado DC-OPF para planificación de contingencias $N-1$.

## 4. Pruebas y Certificación
- **100% Zero-Mockito**: Suites herméticas de prueba sin dependencias de infraestructura.
- **Ejecución:** `mvn clean test`
