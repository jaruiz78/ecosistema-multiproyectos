# Monitor & Predictor Solar Fotovoltaico Este-Oeste (Tocina, Sevilla)

Aplicación web interactiva para visualizar en tiempo real el **máximo teórico (Clear-Sky)** y la **generación horaria real prevista para los próximos 7-14 días** con base en predicciones meteorológicas oficiales de Open-Meteo.

---

## 📍 Configuración de la Instalación

* **Ubicación**: Calle Amadeo Vives 31, Los Rosales - Tocina (Sevilla)
  * **Coordenadas**: `37°35'39" N, 5°44'23" W` (`37.5942, -5.7397`)
  * **Altitud**: 31 m
* **Generador Fotovoltaico (Dual String Este-Oeste)**:
  * **String 1 (Este)**: 6 paneles @ Azimut **85° E** (máximo matinal 09:00 - 13:00)
  * **String 2 (Oeste)**: 4 paneles @ Azimut **265° W** (máximo vespertino 15:00 - 20:30)
  * **Potencia Total Pico**: ~5.00 kWp (a 500 Wp/panel, configurable de 400 a 650 Wp)
* **Inversor Híbrido**: 10.0 kW (doble MPPT)
* **Baterías**: 10.0 kWh (2 módulos Fox-ESS LiFePO4 de 5.0 kWh c/u)

---

## 🚀 Cómo Iniciar la Aplicación Web

### Opción 1: Con Python (Recomendado)
```bash
cd /home/jaruiz/Desarrollo/apps/ProyectoSolarTocina
python3 server.py
```
Abre en tu navegador: [http://localhost:8080](http://localhost:8080)

### Opción 2: Abrir directamente `index.html`
Puedes abrir el archivo [`src/index.html`](file:///home/jaruiz/Desarrollo/apps/ProyectoSolarTocina/src/index.html) en Chrome, Firefox o Safari.

---

## 📊 Características y Funcionalidades

1. **Curva Horaria Detallada**:
   * Curva Máxima Teórica en cielo despejado (*Clear-Sky Model*).
   * Curva de Generación Real Prevista hora a hora según la meteorología (DNI, DHI, nubosidad, temperatura).
   * Desglose independiente de la potencia generada por el **String Este (85°)** frente al **String Oeste (265°)**.
   * Simulación del estado de carga (**SOC %**) de las dos baterías Fox-ESS (10 kWh).
2. **Previsión a 7 Días**:
   * Tarjetas diarias interactivas (kWh generados, % de eficiencia frente al máximo teórico, nubosidad y temperatura).
   * Al pulsar sobre cualquier día de la semana, se desglosa su curva horaria al instante.
3. **Simulador Paramétrico en Tiempo Real**:
   * Ajuste de potencia unitaria por placa (Wp).
   * Ajuste de número de placas por tejado (Este / Oeste).
   * Ajuste del azimut e inclinación del tejado.
   * Modificación de la capacidad de baterías y consumo base del hogar.
4. **Cálculo Físico-Meteorológico Riguroso**:
   * Algoritmo astronómico de posición solar (NOAA / Meeus) en $O(1)$.
   * Descomposición de irradiancia en plano inclinado (*Plane of Array* - POA).
   * Factor de pérdida por temperatura ambiente de Sevilla ($\gamma_{temp} \approx -0.35\%/^\circ\text{C}$).
