#!/usr/bin/env python3
"""
test_proactive_assistant_and_ha.py
==================================
Tests unitarios para el Asistente Proactivo de Notificaciones
y el Exportador MQTT / Discovery para Home Assistant.
"""

import unittest
import json
from proactive_notification_assistant import ProactiveNotificationAssistant
from homeassistant_mqtt_exporter import HomeAssistantMQTTExporter


class TestProactiveAssistantAndHA(unittest.TestCase):

    def test_proactive_assistant_triggers(self):
        """Verifica la generación de alertas ante distintas condiciones de contorno."""
        assistant = ProactiveNotificationAssistant()

        # Condición 1: Gran excedente solar
        state_surplus = {
            "pv_generation_kw": 4.50,
            "home_load_kw": 0.80,
            "battery_soc_pct": 80.0,
            "temp_exterior_c": 27.0,
            "temp_salon_c": 28.9,
            "temp_despacho_c": 30.5,
            "humidity_despacho_pct": 47.0
        }
        alerts = assistant.evaluate_live_triggers(state_surplus)
        ids = [a["id"] for a in alerts]
        self.assertIn("solar_surplus_active", ids)
        self.assertIn("office_dehumidified", ids)

        # Condición 2: Batería llena
        state_battery_full = {
            "pv_generation_kw": 3.50,
            "home_load_kw": 0.60,
            "battery_soc_pct": 99.0,
            "temp_exterior_c": 28.0,
            "temp_salon_c": 26.0,
            "temp_despacho_c": 26.5,
            "humidity_despacho_pct": 50.0
        }
        alerts_full = assistant.evaluate_live_triggers(state_battery_full)
        ids_full = [a["id"] for a in alerts_full]
        self.assertIn("battery_full_export", ids_full)

    def test_homeassistant_mqtt_discovery_payloads(self):
        """Verifica la generación de tópicos y payloads de Home Assistant."""
        exporter = HomeAssistantMQTTExporter(node_id="tocina_solar_twin", base_topic="homeassistant")
        entities = exporter.generate_all_discovery_entities()

        self.assertEqual(len(entities), 7)

        for e in entities:
            self.assertIn("component", e)
            self.assertIn("discovery_topic", e)
            self.assertIn("payload", e)
            payload = e["payload"]
            self.assertIn("name", payload)
            self.assertIn("unique_id", payload)
            self.assertIn("state_topic", payload)
            self.assertIn("device", payload)
            self.assertEqual(payload["device"]["manufacturer"], "Ecosistema Solar Tocina")

    def test_homeassistant_telemetry_serialization(self):
        """Verifica que el payload de telemetría MQTT sea un JSON válido."""
        exporter = HomeAssistantMQTTExporter()
        state = {
            "salon_temp_c": 28.9,
            "despacho_temp_c": 30.5,
            "despacho_hum_pct": 47.0,
            "pv_power_w": 4150,
            "battery_soc_pct": 85.0,
            "net_surplus_w": 3340
        }
        json_str = exporter.export_telemetry_payload(state)
        parsed = json.loads(json_str)
        self.assertEqual(parsed["salon_temp_c"], 28.9)
        self.assertEqual(parsed["pv_power_w"], 4150)
        self.assertEqual(parsed["despacho_hum_pct"], 47.0)


if __name__ == "__main__":
    unittest.main()
