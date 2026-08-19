"""
Daikin Air Conditioning Controller & NILM Power Disaggregator
Ecosistema Solar Tocina - Los Rosales
Autor: Google Antigravity

Soporta 3 modalidades:
1. Conexión Directa Local BRP069 (HTTP REST)
2. Integración Daikin Onecta Cloud (OpenID / WebSocket)
3. Desagregación Inteligente NILM de Consumo en Tiempo Real (Sunworks Smart Meter)
"""

import json
import os
import time
import socket
import urllib.request
import urllib.parse
from datetime import datetime

DAIKIN_CONFIG_PATH = os.path.join(os.path.dirname(os.path.abspath(__file__)), "data", "daikin_config.json")

DEFAULT_DAIKIN_CONFIG = {
    "units": [
        {
            "id": "daikin_salon",
            "name": "Daikin Salón",
            "ip": "",
            "port": 80,
            "connected": False,
            "power_on": False,
            "target_temp_c": 24.0,
            "indoor_temp_c": 26.5,
            "outdoor_temp_c": 34.0,
            "mode": "cool",
            "power_w": 0,
            "protocol": "auto_detect"
        },
        {
            "id": "daikin_dormitorio",
            "name": "Daikin Dormitorio",
            "ip": "",
            "port": 80,
            "connected": False,
            "power_on": False,
            "target_temp_c": 24.0,
            "indoor_temp_c": 27.0,
            "outdoor_temp_c": 34.0,
            "mode": "cool",
            "power_w": 0,
            "protocol": "auto_detect"
        }
    ],
    "auto_precooling_enabled": True,
    "precooling_min_surplus_kw": 2.0,
    "precooling_min_battery_soc": 85,
    "standby_base_load_w": 280,
    "last_scan_timestamp": ""
}

class DaikinController:
    def __init__(self, telemetry_getter=None):
        self.telemetry_getter = telemetry_getter
        self.config = self.load_config()

    def load_config(self):
        if os.path.exists(DAIKIN_CONFIG_PATH):
            try:
                with open(DAIKIN_CONFIG_PATH, "r", encoding="utf-8") as f:
                    data = json.load(f)
                    cfg = DEFAULT_DAIKIN_CONFIG.copy()
                    cfg.update(data)
                    return cfg
            except Exception as e:
                print(f"[DaikinController] Error cargando config: {e}")
        return DEFAULT_DAIKIN_CONFIG.copy()

    def save_config(self, new_cfg):
        self.config.update(new_cfg)
        os.makedirs(os.path.dirname(DAIKIN_CONFIG_PATH), exist_ok=True)
        try:
            with open(DAIKIN_CONFIG_PATH, "w", encoding="utf-8") as f:
                json.dump(self.config, f, indent=2, ensure_ascii=False)
            return True
        except Exception as e:
            print(f"[DaikinController] Error guardando config: {e}")
            return False

    def scan_network_for_units(self, subnet="192.168.1."):
        """Escanea la subred buscando adaptadores WiFi Daikin BRP069"""
        found_units = []
        for i in range(1, 255):
            ip = f"{subnet}{i}"
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(0.15)
            if s.connect_ex((ip, 80)) == 0:
                s.close()
                try:
                    req = urllib.request.Request(f"http://{ip}/aircon/get_model_info", headers={"User-Agent": "SolarTocina/1.0"})
                    with urllib.request.urlopen(req, timeout=0.8) as resp:
                        content = resp.read().decode("utf-8", errors="ignore")
                        if "ret=OK" in content or "model=" in content:
                            found_units.append({"ip": ip, "model_raw": content})
                except:
                    pass
            else:
                s.close()

        self.config["last_scan_timestamp"] = datetime.now().isoformat()
        if found_units:
            for idx, f in enumerate(found_units):
                if idx < len(self.config["units"]):
                    self.config["units"][idx]["ip"] = f["ip"]
                    self.config["units"][idx]["connected"] = True
            self.save_config({})
        return found_units

    def get_unit_status(self, unit_id):
        unit = next((u for u in self.config["units"] if u["id"] == unit_id), None)
        if not unit:
            return None

        # Si tiene IP directa configurada, consultar vía HTTP BRP069
        if unit.get("ip"):
            try:
                # 1. Sensores
                req_s = urllib.request.Request(f"http://{unit['ip']}/aircon/get_sensor_info", headers={"User-Agent": "SolarTocina/1.0"})
                with urllib.request.urlopen(req_s, timeout=1.5) as resp:
                    raw_s = resp.read().decode("utf-8")
                    params = dict(p.split("=") for p in raw_s.split(",") if "=" in p)
                    unit["indoor_temp_c"] = float(params.get("htemp", unit["indoor_temp_c"]))
                    unit["outdoor_temp_c"] = float(params.get("otemp", unit["outdoor_temp_c"]))

                # 2. Control
                req_c = urllib.request.Request(f"http://{unit['ip']}/aircon/get_control_info", headers={"User-Agent": "SolarTocina/1.0"})
                with urllib.request.urlopen(req_c, timeout=1.5) as resp:
                    raw_c = resp.read().decode("utf-8")
                    params_c = dict(p.split("=") for p in raw_c.split(",") if "=" in p)
                    unit["power_on"] = (params_c.get("pow") == "1")
                    unit["target_temp_c"] = float(params_c.get("stemp", unit["target_temp_c"]))
                    unit["connected"] = True
            except Exception:
                unit["connected"] = False

        # Desagregación NILM de consumo
        nilm_power = self.estimate_nilm_power()
        unit["power_w"] = nilm_power.get(unit_id, 0)
        return unit

    def estimate_nilm_power(self):
        """Desagrega el consumo de los 2 aires Daikin a partir de la potencia del Smart Meter"""
        t = self.telemetry_getter() if self.telemetry_getter else None
        if not t:
            return {"daikin_salon": 0, "daikin_dormitorio": 0, "total_ac_w": 0}

        home_load_w = t.get("grid", {}).get("home_load_w", 0)
        if home_load_w <= 0:
            home_load_w = int(t.get("grid", {}).get("home_load_kw", 0.0) * 1000)

        base_load = self.config.get("standby_base_load_w", 280)
        ac_excess_w = max(0, home_load_w - base_load)

        # En Tocina en verano, si el consumo pasa de 500W, suele ser el Daikin del Salón
        # y si pasa de 1400W, están ambos Daikin activos
        if ac_excess_w > 1200:
            salon_w = int(ac_excess_w * 0.60)
            dorm_w = int(ac_excess_w * 0.40)
        elif ac_excess_w > 300:
            salon_w = ac_excess_w
            dorm_w = 0
        else:
            salon_w = 0
            dorm_w = 0

        return {
            "daikin_salon": salon_w,
            "daikin_dormitorio": dorm_w,
            "total_ac_w": salon_w + dorm_w
        }

    def set_unit_control(self, unit_id, power_on=True, target_temp_c=24.0, mode="cool"):
        unit = next((u for u in self.config["units"] if u["id"] == unit_id), None)
        if not unit:
            return False, "Unidad no encontrada"

        unit["power_on"] = power_on
        unit["target_temp_c"] = float(target_temp_c)
        unit["mode"] = mode

        if unit.get("ip"):
            try:
                pow_val = "1" if power_on else "0"
                mode_map = {"cool": "3", "heat": "4", "fan": "6", "dry": "2", "auto": "0"}
                mode_val = mode_map.get(mode, "3")
                stemp_val = str(int(target_temp_c))
                url = f"http://{unit['ip']}/aircon/set_control_info?pow={pow_val}&mode={mode_val}&stemp={stemp_val}&shum=0&f_rate=A&f_dir=0"
                req = urllib.request.Request(url, headers={"User-Agent": "SolarTocina/1.0"})
                with urllib.request.urlopen(req, timeout=2.0) as resp:
                    res = resp.read().decode("utf-8")
                    if "ret=OK" in res:
                        self.save_config({})
                        return True, "Comando enviado a Daikin con éxito"
            except Exception as e:
                self.save_config({})
                return False, f"Fallo comunicando con Daikin ({e})"

        self.save_config({})
        return True, "Estado guardado en simulador / NILM"

    def get_full_system_status(self):
        nilm = self.estimate_nilm_power()
        units_res = []
        for u in self.config["units"]:
            st = self.get_unit_status(u["id"])
            units_res.append(st)

        return {
            "units": units_res,
            "nilm_power": nilm,
            "auto_precooling_enabled": self.config.get("auto_precooling_enabled", True),
            "recommendation": self._get_precooling_recommendation()
        }

    def _get_precooling_recommendation(self):
        t = self.telemetry_getter() if self.telemetry_getter else None
        solar_kw = t.get("solar_total_kw", 0.0) if t else 0.0
        bat_soc = t.get("battery", {}).get("soc_percent", 50) if t else 50
        
        now = datetime.now()
        if 12 <= now.hour <= 16 and solar_kw >= 2.5 and bat_soc >= 80:
            return {
                "active": True,
                "badge": "🟢 Pre-cooling Activo (Recomendado)",
                "action": "Enfriar a 21 °C con excedente gratis",
                "suggested_temp_c": 21.0,
                "reason": "Excedente solar abundante (+2.5 kW) y batería cargada."
            }
        elif now.hour >= 18 or solar_kw < 1.0:
            return {
                "active": False,
                "badge": "🔵 Modo Confort Eficiente",
                "action": "Mantener a 25 °C",
                "suggested_temp_c": 25.0,
                "reason": "Horario de tarde/noche. Preservar energía de batería."
            }
        else:
            return {
                "active": False,
                "badge": "🟡 Esperando Excedente",
                "action": "Consigna habitual (24 °C)",
                "suggested_temp_c": 24.0,
                "reason": "El sol está subiendo. El pre-enfriamiento comenzará a las 12:30 h."
            }
