# core-geogrid-h3 — Motor Geoespacial H3 Volumétrico 3D, Geodesia WGS-84 & Física Topográfica

Módulo algorítmico puro en **Java 25 LTS** para la indexación espacial volumétrica sobre mallas hexagonales **Uber H3**, cálculo de distancias geodésicas elipsoidales de alta precisión mediante el **Algoritmo de Vincenty** y balances de pérdidas hidráulicas y energéticas.

---

## 1. Fundamentos Teóricos y Modelos Físicos

El motor combina el sistema de teselación discreta hexagonal H3 en 2D con un eje vertical cuantizado en bandas altimétricas, permitiendo modelar prismas espaciales 3D para logística (AppViajes) e hidráulica (SaaSRegantes).

### Ecuaciones Gobernantes

1. **Geodesia Elipsoidal WGS-84 (Algoritmo de Vincenty):**
   Calcula la distancia geodésica entre dos puntos sobre el elipsoide WGS-84 (\(a = 6378137.0\text{ m}\), \(f = 1/298.257223563\), \(b = a(1-f)\)) mediante convergencia iterativa de las ecuaciones geodésicas directas e inversas:
   \[ \tan \sigma = \frac{\sqrt{(\cos U_2 \sin \lambda)^2 + (\cos U_1 \sin U_2 - \sin U_1 \cos U_2 \cos \lambda)^2}}{\sin U_1 \sin U_2 + \cos U_1 \cos U_2 \cos \lambda} \]
   con umbral de tolerancia fijado a \(\Delta \lambda < 10^{-12}\).

2. **Indexación Volumétrica 3D:**
   Dada una celda H3 \(H\) y una altitud \(z\), la clave espacial compuesta se cuantiza en bandas \(\Delta h\):
   \[ \text{Band}_z = \left\lfloor \frac{z}{\Delta h} \right\rfloor \cdot \Delta h \implies \text{Key}_{\text{3D}} = H \mathbin{:} \text{Z}\text{Band}_z \]

3. **Gasto Mecánico Vehicular en Pendiente (AppViajes):**
   \[ E_{\text{joules}} = m \cdot g \cdot \Delta h + \mu \cdot m \cdot g \cdot \cos(\theta) \cdot d_{\text{3D}} \]
   donde \(m\) es la masa del vehículo, \(g = 9.80665\text{ m/s}^2\), \(\Delta h\) el desnivel topográfico, \(\mu\) el coeficiente de rozamiento dinámico y \(d_{\text{3D}}\) la distancia tridimensional euclídea.

4. **Pérdida de Carga Hidráulica en Tuberías Presurizadas (SaaSRegantes):**
   Implementa la formulación universal de **Darcy-Weisbach**:
   \[ h_f = f \cdot \left(\frac{L}{D}\right) \cdot \left(\frac{v^2}{2g}\right) \]
   donde \(h_f\) es la pérdida en metros de columna de agua (m.c.a.), \(f\) el factor de fricción de Darcy, \(L\) la longitud de la tubería en metros, \(D\) el diámetro interno y \(v\) la velocidad media de flujo en m/s.

---

## 2. Componentes Principales

- **`H3Spatial3DGridEngine`**:
  - `calculateVincentyDistanceMeters(lat1, lon1, lat2, lon2)`: Distancia geodésica WGS-84 submilimétrica.
  - `createCell(h3Index, lat, lon, elevationMeters, bandStepMeters)`: Factoría determinista de celdas volumétricas `H3VolumetricCell`.
  - `calculate3DVector(origin, dest)`: Vector espacial con distancia 2D, desnivel, distancia 3D y pendiente topográfica porcentual.
  - `calculateVehicleEnergyJoules(massKg, vector, rollingResistanceCoeff)`: Balance energético mecánico.
  - `calculateHydraulicHeadLossMeters(pipeLength, diameter, velocity, frictionFactor)`: Cálculo hidráulico directo en \(O(1)\).
- **Pureza de Dominio:**
  - Registros inmutables en **Java 25 Records** (`H3VolumetricCell`, `Spatial3DVector`).
  - Precondiciones de Hoare y validaciones invariantes sin dependencias de infraestructura.

---

## 3. Pruebas y Certificación

- **Testing Hermético (Zero-Mockito):**
  ```bash
  mvn clean test
  ```