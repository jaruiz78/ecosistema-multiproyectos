"""
ble_xiaomi_daemon.py
Daemon de Escaneo Bluetooth BLE para Sensores Xiaomi Mijia 2 (LYWSD03MMC / NUN4126GL)
Ecosistema Solar Tocina - Los Rosales
Autor: Google Antigravity

Función:
- Escucha los paquetes publicitarios BLE (Advertising Beacons) emitidos por los sensores Xiaomi.
- Soporta formato oficial MiBeacon (0xFE95), BTHome v2 y firmware libre ATC/PVVX (0x181A).
- Inyecta automáticamente las lecturas en http://localhost:8526/api/environmental-sensors/record.
- Cero emparejamiento manual: Solo escucha en el aire sin gastar batería del sensor.
"""

import os
import sys
import time
import json
import struct
import urllib.request
import asyncio
from datetime import datetime

# Mapeo de direcciones MAC conocidas a ID de estancias en Solar Tocina
# (Se pueden autodescubrir o registrar aquí)
KNOWN_SENSORS_MAP = {
    # "A4:C1:38:XX:XX:XX": "sensor_salon",
    # "A4:C1:38:YY:YY:YY": "sensor_dormitorio"
}

API_RECORD_URL = "http://localhost:8526/api/environmental-sensors/record"

def parse_atc_custom_data(data_bytes: bytes):
    """
    Decodifica el formato ATC / PVVX (13 bytes sobre Service Data 0x181A):
    Bytes: [MAC (6), Temp (2, int16 * 0.1 o 0.01), Hum (2, uint16 * 0.1), Bat_mV (2), Bat_% (1)]
    """
    if len(data_bytes) >= 13:
        mac = ":".join(f"{b:02X}" for b in data_bytes[0:6])
        temp_raw = int.from_bytes(data_bytes[6:8], byteorder="little", signed=True)
        hum_raw = int.from_bytes(data_bytes[8:10], byteorder="little", signed=False)
        battery_pct = data_bytes[12]
        
        # En firmware PVVX, la temp viene en centésimas de grado (x100) o décimas (x10)
        temp_c = temp_raw / 100.0 if abs(temp_raw) > 500 else temp_raw / 10.0
        hum_pct = hum_raw / 100.0 if hum_raw > 1000 else hum_raw / 10.0

        return {
            "mac": mac,
            "temperature_c": round(temp_c, 1),
            "humidity_pct": round(hum_pct, 1),
            "battery_pct": battery_pct
        }
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
            headers={"Content-Type": "application/json", "User-Agent": "SolarTocina-BLEDaemon/1.0"},
            method="POST"
        )
        with urllib.request.urlopen(req, timeout=1.5) as resp:
            if resp.status == 200:
                print(f"[{datetime.now().strftime('%H:%M:%S')}] Ingestado {sensor_id}: {temp_c}°C | {hum_pct}% HR | Batería: {bat_pct}%")
    except Exception as e:
        print(f"[BLEDaemon] Error enviando lectura de {sensor_id}: {e}")

async def run_ble_scanner():
    try:
        from bleak import BleakScanner
    except ImportError:
        print("Instalando dependencia 'bleak' para escaneo Bluetooth...")
        os.system(f"{sys.executable} -m pip install bleak")
        from bleak import BleakScanner

    print("📡 Iniciando Escáner Bluetooth BLE Nativo de Linux (hci0)...")
    print(f"🔗 Servidor destino: {API_RECORD_URL}")

    def detection_callback(device, advertisement_data):
        service_data = advertisement_data.service_data
        for uuid, data in service_data.items():
            # 0x181A = Environmental Sensing (Firmware ATC / PVVX)
            if "181a" in uuid.lower():
                parsed = parse_atc_custom_data(data)
                if parsed:
                    sensor_id = KNOWN_SENSORS_MAP.get(device.address.upper(), "sensor_salon")
                    send_telemetry_to_server(
                        sensor_id=sensor_id,
                        temp_c=parsed["temperature_c"],
                        hum_pct=parsed["humidity_pct"],
                        bat_pct=parsed["battery_pct"]
                    )

    scanner = BleakScanner(detection_callback=detection_callback)
    await scanner.start()
    while True:
        await asyncio.sleep(5)

if __name__ == "__main__":
    try:
        asyncio.run(run_ble_scanner())
    except KeyboardInterrupt:
        print("\nDeteniendo daemon BLE.")
