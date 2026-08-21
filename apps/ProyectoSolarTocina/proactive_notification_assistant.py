#!/usr/bin/env python3
"""
proactive_notification_assistant.py
===================================
Asistente Proactivo de Hogar Inteligente & Notificaciones Predictivas.

Analiza el estado combinado del Gemelo Digital:
- Sensores térmicos en tiempo real (Salón, Despacho, Cochera, Patio)
- Excedente fotovoltaico actual y predicción a 60 min
- Estado de carga de la batería Fox-ESS (SoC)
- Temperatura exterior y ángulo solar

Genera avisos accionables y concisos listos para Telegram, WhatsApp o Home Assistant.
"""

from typing import Dict, List, Any
from datetime import datetime


class ProactiveNotificationAssistant:
    def __init__(self):
        pass

    def evaluate_live_triggers(self, current_state: Dict[str, Any]) -> List[Dict[str, Any]]:
        """
        Evalúa el estado del hogar y genera una lista priorizada de avisos inteligentes.
        """
        now = datetime.now()
        hour = now.hour

        pv_kw = current_state.get("pv_generation_kw", 4.15)
        load_kw = current_state.get("home_load_kw", 0.81)
        batt_soc_pct = current_state.get("battery_soc_pct", 75.0)
        t_ext_c = current_state.get("temp_exterior_c", 27.0)
        t_salon_c = current_state.get("temp_salon_c", 28.9)
        t_despacho_c = current_state.get("temp_despacho_c", 30.5)
        humidity_despacho_pct = current_state.get("humidity_despacho_pct", 47.0)

        surplus_kw = max(0.0, pv_kw - load_kw)
        alerts = []

        # Trigger 1: Gran Excedente Solar Disponible
        if surplus_kw >= 2.0:
            alerts.append({
                "id": "solar_surplus_active",
                "priority": "HIGH",
                "category": "⚡ Energía Solar Gratuita",
                "emoji": "☀️",
                "title": f"Excedente Solar de {round(surplus_kw, 2)} kW Disponible",
                "message": f"Tienes {round(surplus_kw, 2)} kW de excedente solar limpio. Momento ideal para poner lavavajillas, lavadora, horno o activar pre-cooling a coste 0.00 €.",
                "action_type": "APPLIANCE_DISPATCH",
                "timestamp": now.strftime("%H:%M")
            })

        # Trigger 2: Deshumidificación Exitosa en Despacho
        if humidity_despacho_pct <= 48.0:
            alerts.append({
                "id": "office_dehumidified",
                "priority": "MEDIUM",
                "category": "🌬️ Clima y Confort",
                "emoji": "❄️",
                "title": f"Despacho Deshumidificado al {round(humidity_despacho_pct, 1)}%",
                "message": "El flujo de aire seco del salón ha renovado el despacho. Mantén la ventana cerrada para consolidar el descenso térmico.",
                "action_type": "WINDOW_CONTROL",
                "timestamp": now.strftime("%H:%M")
            })

        # Trigger 3: Free-Cooling Nocturno (T_ext < T_salon de noche)
        if (hour >= 22 or hour < 7) and t_ext_c < (t_salon_c - 1.5):
            alerts.append({
                "id": "free_cooling_opportunity",
                "priority": "HIGH",
                "category": "🌙 Ventilación Gratuita",
                "emoji": "🍃",
                "title": f"Free-Cooling Nocturno Activo ({round(t_ext_c, 1)}°C Exterior)",
                "message": f"El exterior ({round(t_ext_c, 1)}°C) está más fresco que la vivienda ({round(t_salon_c, 1)}°C). Abre balcón Este y terraza Norte para enfriar por tiro natural sin gasto.",
                "action_type": "VENTILATION_DISPATCH",
                "timestamp": now.strftime("%H:%M")
            })

        # Trigger 4: Batería Fox-ESS al 100% (Sobrealimentación a Batería Virtual)
        if batt_soc_pct >= 98.0 and surplus_kw > 1.0:
            alerts.append({
                "id": "battery_full_export",
                "priority": "LOW",
                "category": "🔋 Batería & Finanzas",
                "emoji": "🏦",
                "title": "Batería Fox-ESS al 100% • Acumulando Saldo Virtual",
                "message": f"Tu batería física está llena. Todo el excedente actual ({round(surplus_kw, 2)} kW) se está abonando como saldo monetario en tu Batería Virtual Naturgy.",
                "action_type": "VIRTUAL_BATTERY_INFO",
                "timestamp": now.strftime("%H:%M")
            })

        # Trigger 5: Persianas Fachada Este en la Mañana
        if 8 <= hour <= 13:
            alerts.append({
                "id": "east_shading_morning",
                "priority": "MEDIUM",
                "category": "🪟 Bioclimática Pasiva",
                "emoji": "🛡️",
                "title": "Protección Solar Fachada Este (89° E)",
                "message": "Sol matinal incidiendo sobre la calle. Mantener persianas de fachada delantera bajadas al 85% para frenar ganancia de calor.",
                "action_type": "SHADING_DISPATCH",
                "timestamp": now.strftime("%H:%M")
            })

        return alerts


if __name__ == "__main__":
    assistant = ProactiveNotificationAssistant()
    state = {
        "pv_generation_kw": 4.20,
        "home_load_kw": 0.81,
        "battery_soc_pct": 82.0,
        "temp_exterior_c": 27.0,
        "temp_salon_c": 28.9,
        "temp_despacho_c": 30.5,
        "humidity_despacho_pct": 47.0
    }
    alerts = assistant.evaluate_live_triggers(state)
    print(f"✅ Asistente Proactivo generó {len(alerts)} alertas en vivo:")
    for a in alerts:
        print(f" • [{a['priority']}] {a['emoji']} {a['title']}: {a['message']}")
