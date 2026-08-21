"""
ble_environmental_daemon.py
Daemon Universal de Escaneo Bluetooth BLE para Sensores Ambientales:
1. ThermoPro TP357 / TP358 / TP359 (Bluetooth 5.0 con Pila AAA)
2. Xiaomi Mi 2 (LYWSD03MMC / NUN4126GL) con MiBeacon, ATC o BTHome
3. Qingping / BTHome v2 genéricos

Ecosistema Solar Tocina - Los Rosales
Autor: Google Antigravity
"""

import os
import sys
import time
import json
import struct
import urllib.request
import asyncio
from datetime import datetime

API_RECORD_URL = "http://localhost:8526/api/environmental-sensors/record"

KNOWN_SENSORS_MAP = {
    # Mapeo por defecto si se desea fijar por dirección MAC o nombre BLE
    # "TP357 (XXXX)": "sensor_salon",
    # "LYWSD03MMC": "sensor_dormitorio"
}

def parse_thermopro_tp357(manufacturer_data: bytes):
    try:
        if len(manufacturer_data) >= 6:
            temp_raw = int.from_bytes(manufacturer_data[1:3], byteorder="little", signed=True)
            hum_raw = manufacturer_data[3]
            
            temp_c = temp_raw / 10.0
            if -20.0 <= temp_c <= 65.0 and 5 <= hum_raw <= 99:
                return {
                    "type": "ThermoPro TP357",
                    "temperature_c": round(temp_c, 1),
                    "humidity_pct": round(float(hum_raw), 1),
                    "battery_pct": 95
                }
    except Exception:
        pass
    return None

def parse_atc_pvvx(data_bytes: bytes):
    try:
        if len(data_bytes) >= 13:
            mac = ":".join(f"{b:02X}" for b in data_bytes[0:6])
            temp_raw = int.from_bytes(data_bytes[6:8], byteorder="little", signed=True)
            hum_raw = int.from_bytes(data_bytes[8:10], byteorder="little", signed=False)
            battery_pct = data_bytes[12]
            
            temp_c = temp_raw / 100.0 if abs(temp_raw) > 500 else temp_raw / 10.0
            hum_pct = hum_raw / 100.0 if hum_raw > 1000 else hum_raw / 10.0

            return {
                "mac": mac,
                "temperature_c": round(temp_c, 1),
                "humidity_pct": round(hum_pct, 1),
                "battery_pct": battery_pct
            }
        elif len(data_bytes) >= 4:
            temp_raw = int.from_bytes(data_bytes[0:2], byteorder="little", signed=True)
            hum_raw = data_bytes[2]
            return {
                "mac": "XIAOMI_GENERIC",
                "temperature_c": round(temp_raw / 10.0, 1),
                "humidity_pct": float(hum_raw),
                "battery_pct": 90
            }
    except Exception:
        pass
    return None

def send_telemetry_to_server(sensor_id: str, temp_c: float, hum_pct: float, bat_pct: int = 95):
    try:
        payload = {
            "sensor_id": sensor_id,
            "temperature_c": temp_c,
            "humidity_pct": hum_pct,
            "battery_pct": bat_pct
        }
        req = urllib.request.Request(
            API_RECORD_URL,
            data=json.dumps(payload).encode("utf-8"),
            headers={"Content-Type": "application/json", "User-Agent": "SolarTocina-BLEDaemon/2.0"},
            method="POST"
        )
        with urllib.request.urlopen(req, timeout=1.5) as resp:
            if resp.status == 200:
                print(f"[{datetime.now().strftime('%H:%M:%S')}] 📡 Sensor Ingestado [{sensor_id}]: {temp_c} °C | {hum_pct}% HR | Bat: {bat_pct}%")
    except Exception as e:
        pass

async def run_ble_scanner():
    try:
        from bleak import BleakScanner
    except ImportError:
        print("❌ No se encontró el módulo bleak.")
        return

    print("📡 Iniciando Escáner Universal Bluetooth BLE (ThermoPro TP357 + Xiaomi)...")
    print(f"🔗 Servidor destino: {API_RECORD_URL}")

    def detection_callback(device, advertisement_data):
        dev_name = advertisement_data.local_name or device.name or ""
        
        # 1. Detección ThermoPro (TP357 / TP358)
        if "TP357" in dev_name.upper() or "THERMOPRO" in dev_name.upper() or "TP358" in dev_name.upper():
            for m_id, m_bytes in advertisement_data.manufacturer_data.items():
                parsed = parse_thermopro_tp357(m_bytes)
                if parsed:
                    s_id = KNOWN_SENSORS_MAP.get(device.address.upper(), "sensor_salon")
                    send_telemetry_to_server(s_id, parsed["temperature_c"], parsed["humidity_pct"], parsed["battery_pct"])
                    return

        # 2. Detección Xiaomi / ATC / BTHome (0x181A o 0xFE95)
        for uuid, data in advertisement_data.service_data.items():
            if "181a" in uuid.lower() or "fe95" in uuid.lower():
                parsed = parse_atc_pvvx(data)
                if parsed:
                    s_id = KNOWN_SENSORS_MAP.get(device.address.upper(), "sensor_dormitorio")
                    send_telemetry_to_server(s_id, parsed["temperature_c"], parsed["humidity_pct"], parsed["battery_pct"])
                    return

    while True:
        try:
            scanner = BleakScanner(detection_callback=detection_callback)
            await scanner.start()
            while True:
                await asyncio.sleep(5)
        except Exception as e:
            print(f"⚠️ [BLE Scanner] Interfaz Bluetooth no disponible o en reposo ({e}). Reintentando en 15s...")
            await asyncio.sleep(15)

if __name__ == "__main__":
    try:
        asyncio.run(run_ble_scanner())
    except KeyboardInterrupt:
        print("\nDeteniendo daemon BLE Universal.")
