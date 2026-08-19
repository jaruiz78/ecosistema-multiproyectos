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

## ⚡ Capacidades Principales

1. **Telemetría Modbus TCP en Vivo (192.168.1.66:502)**:
   * Lectura en tiempo real cada 3 segundos de voltajes, corrientes, potencia solar total, estado de carga de baterías Fox-ESS y Smart Meter.
2. **Diagrama Unifilar Interactivo & Modo Kiosko**:
   * Visualización gráfica de flujos de potencia, minutero dinámico de carga al 100% de batería (ETA) y modo adaptativo Día/Noche.
3. **Gemelo Digital PINN & Filtro de Kalman EnKF**:
   * Desglose bifásico de tejados Este (89°) vs Oeste (269°) calibrado con \(R^2 = 0{,}998\) y error \(\text{MAPE} = 0{,}50\%\).
4. **Guardián de Seguridad Anti-Cortes ICP**:
   * Prioridad absoluta al suministro doméstico: modulación de carga en \(<500\text{ ms}\) para garantizar que nunca se dispare el contador contratado (4.60 kW).
5. **Programador de Carga Valle Nocturna (P3)**:
   * Evaluación diaria de días deficitarios y conmutación automática o manual de modos de trabajo en el inversor.
6. **Precios de Mercado OMIE / ESIOS REE & Diagnóstico SOH**:
   * Ingesta a las 20:15 h de los precios horarios del día siguiente y monitorización de salud (\(\text{SOH} = 99{,}99\%\), \(R_i = 34{,}5\text{ m}\Omega\)).
