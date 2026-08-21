# ☀️ Ecosistema Solar Tocina & Gemelo Digital Fotovoltaico
### Plataforma de Alta Concurrencia, Telemetría Modbus TCP e Inteligencia Artificial
**Ubicación:** C/ Amadeo Vives 31, Los Rosales - Tocina (Sevilla) · `37°35′39″ N, 5°44′23″ O` (Altitud 31 m, H3: `8939023447bffff`)  
**Hardware:** 10x Jinko 500W TOPCon (6 Este / 4 Oeste) · Inversor Sunworks KP10 SW (10 kW) · 2x Fox-ESS EP5 HV (10.36 kWh) · Omoda 7 SHS (18.7 kWh) · 2x Daikin Inverter · ICP 4.60 kW

---

## 🚀 Inicio Rápido

El servidor local opera como un servicio autónomo en el puerto **`8526`**:

```bash
# Iniciar servidor local
cd /home/jaruiz/Desarrollo/apps/ProyectoSolarTocina
python3 server.py 8526
```

Acceder en el navegador: **[http://localhost:8526](http://localhost:8526)**

---

## 📑 Documentación Completa

* 📘 **[Documentación Maestra del Ecosistema](file:///home/jaruiz/Desarrollo/apps/ProyectoSolarTocina/docs/DOCUMENTACION_MAESTRA_ECOSISTEMA_SOLAR_TOCINA.md)**: Especificaciones físicas, fórmulas del modelo PINN, arquitectura de software, endpoints y manual de mantenimiento.
* 🛠️ **[Guía de Hardware, Automatización y Sensores](file:///home/jaruiz/Desarrollo/apps/ProyectoSolarTocina/docs/GUIA_AUTOMATIZACION_HARDWARE_Y_SENSORES.md)**: Conexión WiFi Daikin (Faikin/IR), Wallbox Omoda 7, Carga en horas valle P3 y sondas microclimáticas.

---

## ⚡ Capacidades Principales (Versión 5.0)

1. **Telemetría Modbus TCP en Vivo (192.168.1.66:502)**:
   * Lectura en tiempo real cada 3 segundos de voltajes, corrientes, potencia solar total, estado de carga de baterías Fox-ESS y Smart Meter.
2. **Optimizador Predictivo MPC en Horizonte Rodante (48 Horas)** (`mpc_rolling_horizon_optimizer.py`):
   * Despacho conjunto óptimo: Carga de batería Fox-ESS, pre-cooling Daikin, carga solar del Omoda 7 y depósito en Batería Virtual Naturgy.
3. **Difusión Térmica de Fourier 1D/2D & Desfase de Forjados** (`fourier_pinn_wall_diffusion.py`):
   * Modelado numérico transitorio de cerramientos multicapa: calcula el desfase térmico de \(11{,}0\text{ h}\) en la cubierta/terraza superior y \(10{,}0\text{ h}\) en fachada Norte (`359° N`).
4. **Nowcasting Solar Satelital a Muy Corto Plazo (15–60 min)** (`aemet_radar_satellite_service.py`):
   * Vector de movimiento de masas nubosas y estimación de riesgo de oclusión solar a +15m, +30m, +45m y +60m.
5. **Asistente Proactivo de Hogar & Notificaciones** (`proactive_notification_assistant.py`):
   * Generación en tiempo real de recomendaciones accionables (excedente solar, deshumidificación, *free-cooling* y persianas).
6. **Autodescubrimiento MQTT para Home Assistant** (`homeassistant_mqtt_exporter.py`):
   * 7 entidades nativas de telemetría y sensores ambientales para integración local sin configuración YAML.
7. **Gemelo Digital PINN & Filtro de Kalman EnKF Multizona** (`kalman_multizone_twin.py`):
   * Asimilación estocástica continua en 6 zonas térmicas con reducción de covarianza (\(\text{Trace}(P) < 0{,}50\)).
8. **Guardián de Seguridad Anti-Cortes ICP & Carga Valle Nocturna**:
   * Protección activa del término de potencia contratada (\(4{,}60\text{ kW}\)) y programación de carga en horas valle P3.
9. **Suite Completa de Testing**:
   * **31 tests unitarios e integrados (100% en verde)** ejecutables vía `PYTHONPATH=. python3 -m pytest tests/`.

