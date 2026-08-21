#!/usr/bin/env python3
"""
homeassistant_mqtt_exporter.py
==============================
Generador de Autodescubrimiento MQTT y Esquema REST para Home Assistant.

Permite que cualquier instancia de Home Assistant en la red local descubra
automáticamente todos los sensores, estados térmicos y magnitudes solares
del ecosistema de Tocina sin configuración manual YAML.
"""

from typing import Dict, List, Any
import json


class HomeAssistantMQTTExporter:
    def __init__(self, node_id: str = "tocina_solar_twin", base_topic: str = "homeassistant"):
        self.node_id = node_id
        self.base_topic = base_topic

    def generate_all_discovery_entities(self) -> List[Dict[str, Any]]:
        """
        Genera la lista completa de entidades con sus esquemas MQTT Discovery para HA.
        """
        device_info = {
            "identifiers": ["proyectoSolarTocina_digital_twin_v2"],
            "name": "Gemelo Digital Solar Tocina",
            "model": "PINN Multizona + Fox-ESS + Daikin",
            "manufacturer": "Ecosistema Solar Tocina",
            "sw_version": "2.5.0"
        }

        entities = [
            # 1. Sensores de Temperatura
            {
                "component": "sensor",
                "object_id": "salon_temperature",
                "discovery_topic": f"{self.base_topic}/sensor/{self.node_id}/salon_temp/config",
                "payload": {
                    "name": "Salón Temperatura (ThermoPro)",
                    "unique_id": f"{self.node_id}_salon_temp",
                    "device_class": "temperature",
                    "state_class": "measurement",
                    "unit_of_measurement": "°C",
                    "state_topic": f"solar_tocina/{self.node_id}/telemetry",
                    "value_template": "{{ value_json.salon_temp_c }}",
                    "device": device_info
                }
            },
            {
                "component": "sensor",
                "object_id": "despacho_temperature",
                "discovery_topic": f"{self.base_topic}/sensor/{self.node_id}/despacho_temp/config",
                "payload": {
                    "name": "Despacho Temperatura",
                    "unique_id": f"{self.node_id}_despacho_temp",
                    "device_class": "temperature",
                    "state_class": "measurement",
                    "unit_of_measurement": "°C",
                    "state_topic": f"solar_tocina/{self.node_id}/telemetry",
                    "value_template": "{{ value_json.despacho_temp_c }}",
                    "device": device_info
                }
            },
            {
                "component": "sensor",
                "object_id": "despacho_humidity",
                "discovery_topic": f"{self.base_topic}/sensor/{self.node_id}/despacho_hum/config",
                "payload": {
                    "name": "Despacho Humedad Relativa",
                    "unique_id": f"{self.node_id}_despacho_hum",
                    "device_class": "humidity",
                    "state_class": "measurement",
                    "unit_of_measurement": "%",
                    "state_topic": f"solar_tocina/{self.node_id}/telemetry",
                    "value_template": "{{ value_json.despacho_hum_pct }}",
                    "device": device_info
                }
            },
            # 2. Sensores Eléctricos y Fotovoltaicos
            {
                "component": "sensor",
                "object_id": "solar_pv_power",
                "discovery_topic": f"{self.base_topic}/sensor/{self.node_id}/pv_power/config",
                "payload": {
                    "name": "Potencia Fotovoltaica Tejado (Jinko 5kWp)",
                    "unique_id": f"{self.node_id}_pv_power",
                    "device_class": "power",
                    "state_class": "measurement",
                    "unit_of_measurement": "W",
                    "state_topic": f"solar_tocina/{self.node_id}/telemetry",
                    "value_template": "{{ value_json.pv_power_w }}",
                    "device": device_info
                }
            },
            {
                "component": "sensor",
                "object_id": "fox_ess_battery_soc",
                "discovery_topic": f"{self.base_topic}/sensor/{self.node_id}/battery_soc/config",
                "payload": {
                    "name": "Batería Fox-ESS Estado de Carga",
                    "unique_id": f"{self.node_id}_batt_soc",
                    "device_class": "battery",
                    "state_class": "measurement",
                    "unit_of_measurement": "%",
                    "state_topic": f"solar_tocina/{self.node_id}/telemetry",
                    "value_template": "{{ value_json.battery_soc_pct }}",
                    "device": device_info
                }
            },
            {
                "component": "sensor",
                "object_id": "solar_net_surplus",
                "discovery_topic": f"{self.base_topic}/sensor/{self.node_id}/net_surplus/config",
                "payload": {
                    "name": "Excedente Solar Neto",
                    "unique_id": f"{self.node_id}_net_surplus",
                    "device_class": "power",
                    "state_class": "measurement",
                    "unit_of_measurement": "W",
                    "state_topic": f"solar_tocina/{self.node_id}/telemetry",
                    "value_template": "{{ value_json.net_surplus_w }}",
                    "device": device_info
                }
            },
            # 3. Sensor de Recomendación Bioclimática
            {
                "component": "sensor",
                "object_id": "bioclimatic_advice",
                "discovery_topic": f"{self.base_topic}/sensor/{self.node_id}/bioclimatic_advice/config",
                "payload": {
                    "name": "Recomendación Bioclimática Activa",
                    "unique_id": f"{self.node_id}_bioclimatic_advice",
                    "icon": "mdi:home-thermometer",
                    "state_topic": f"solar_tocina/{self.node_id}/telemetry",
                    "value_template": "{{ value_json.bioclimatic_advice_text }}",
                    "device": device_info
                }
            }
        ]

        return entities

    def export_telemetry_payload(self, state: Dict[str, Any]) -> str:
        """Serializa el payload de telemetría a formato JSON MQTT."""
        return json.dumps({
            "salon_temp_c": state.get("salon_temp_c", 28.9),
            "despacho_temp_c": state.get("despacho_temp_c", 30.5),
            "despacho_hum_pct": state.get("despacho_hum_pct", 47.0),
            "pv_power_w": state.get("pv_power_w", 4150),
            "battery_soc_pct": state.get("battery_soc_pct", 80.0),
            "net_surplus_w": state.get("net_surplus_w", 3340),
            "bioclimatic_advice_text": state.get("bioclimatic_advice_text", "Pre-cooling Solar Activo")
        })


if __name__ == "__main__":
    exporter = HomeAssistantMQTTExporter()
    entities = exporter.generate_all_discovery_entities()
    print(f"✅ Generadas {len(entities)} entidades de autodescubrimiento Home Assistant MQTT:")
    for e in entities:
        print(f" • [{e['component'].upper()}] {e['payload']['name']} -> {e['discovery_topic']}")
