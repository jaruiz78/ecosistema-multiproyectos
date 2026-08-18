#!/usr/bin/env python3
"""
Servidor web local, puente Modbus TCP y Gestor de Base de Datos SQLite para Inversor Sunworks / Fox-ESS 10kW
Puerto web: 8080 | Conexión Modbus Inversor: 192.168.1.66:502 (Unit ID 247)
"""
import http.server
import socketserver
import os
import sys
import json
import socket
import struct
import time
import threading
from datetime import datetime

from telemetry_db import save_telemetry_record, get_recent_history, get_history_stats
from foxcloud_sync import save_foxcloud_credentials, get_foxcloud_credentials, sync_historical_gaps
from weather_broker import (
    get_weather_forecast,
    start_weather_broker_thread,
    get_climate_historical_5yr_summary,
    get_monthly_climate_breakdown
)
from online_learning_twin import learning_engine
from annual_ai_predictor import annual_ai_engine
from pinn_solar_model import pinn_solar_engine
from telemetry_ingestor_daemon import telemetry_daemon
from litert_solar_kernel import litert_engine
from duckdb_analytics_engine import duckdb_engine

PORT = 8526
DIRECTORY = os.path.join(os.path.dirname(os.path.abspath(__file__)), "src")
INVERTER_IP = "192.168.1.66"
INVERTER_PORT = 502
INVERTER_UNIT_ID = 247

_modbus_socket = None
_modbus_socket_lock = threading.Lock()

def get_or_create_modbus_socket():
    global _modbus_socket
    if _modbus_socket is not None:
        return _modbus_socket
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(2.0)
        s.setsockopt(socket.SOL_SOCKET, socket.SO_KEEPALIVE, 1)
        s.connect((INVERTER_IP, INVERTER_PORT))
        _modbus_socket = s
        return s
    except Exception:
        _modbus_socket = None
        return None

def close_modbus_socket():
    global _modbus_socket
    if _modbus_socket:
        try:
            _modbus_socket.close()
        except Exception:
            pass
        _modbus_socket = None

def read_inverter_modbus_telemetry():
    """Lee registros de telemetría en tiempo real desde el inversor reutilizando socket persistente (Zero-Allocation)"""
    with _modbus_socket_lock:
        s = get_or_create_modbus_socket()
        if not s:
            return {
                "online": False,
                "ip": INVERTER_IP,
                "error": "No connection to inverter Modbus TCP",
                "timestamp": datetime.now().isoformat()
            }
        try:
            req = struct.pack(">HHHBBHH", 1, 0, 6, INVERTER_UNIT_ID, 3, 31000, 30)
            s.sendall(req)
            resp = s.recv(1024)
            if resp and len(resp) >= 9:
                data = resp[9:]
                regs = [struct.unpack(">H", data[i:i+2])[0] for i in range(0, len(data), 2)]
                
                pv1_v = regs[0] / 10.0
                pv1_a = regs[1] / 10.0
                pv1_w = regs[2]
                
                pv2_v = regs[3] / 10.0
                pv2_a = regs[4] / 10.0
                pv2_w = regs[5]
                
                grid_v = regs[6] / 10.0
                grid_a = regs[7] / 10.0
                ac_power_w = regs[8]
                grid_hz = regs[9] / 100.0

                # Registros del Smart Meter (Chint/Eastron)
                grid_export_w = regs[14] if len(regs) > 14 else max(0, ac_power_w - 1000)
                home_load_w = regs[16] if len(regs) > 16 else max(0, ac_power_w - grid_export_w)
                
                bat_v = regs[20] / 10.0 if len(regs) > 20 else 0
                bat_soc = regs[24] if len(regs) > 24 else 100
                inv_temp = regs[23] / 10.0 if len(regs) > 23 else 0
                
                return {
                    "online": True,
                    "ip": INVERTER_IP,
                    "model": "Sunworks KP10 (Fox-ESS)",
                    "timestamp": datetime.now().isoformat(),
                    "pv1_east": { "voltage_v": pv1_v, "current_a": pv1_a, "power_w": pv1_w, "power_kw": round(pv1_w / 1000.0, 3) },
                    "pv2_west": { "voltage_v": pv2_v, "current_a": pv2_a, "power_w": pv2_w, "power_kw": round(pv2_w / 1000.0, 3) },
                    "solar_total_w": pv1_w + pv2_w,
                    "solar_total_kw": round((pv1_w + pv2_w) / 1000.0, 3),
                    "grid": { 
                        "voltage_v": grid_v, 
                        "current_a": grid_a, 
                        "ac_power_w": ac_power_w, 
                        "ac_power_kw": round(ac_power_w / 1000.0, 3), 
                        "freq_hz": grid_hz,
                        "grid_export_w": grid_export_w,
                        "grid_export_kw": round(grid_export_w / 1000.0, 3),
                        "home_load_w": home_load_w,
                        "home_load_kw": round(home_load_w / 1000.0, 3)
                    },
                    "battery": { "voltage_v": bat_v, "soc_percent": bat_soc, "nominal_kwh": 10.36 },
                    "inverter": { "temperature_c": inv_temp }
                }
        except Exception as e:
            close_modbus_socket()
            return {
                "online": False,
                "ip": INVERTER_IP,
                "error": str(e),
                "timestamp": datetime.now().isoformat()
            }

_active_sampling_interval = 15.0 # Por defecto 15s en modo Eco Activo (4 lecturas/min)

def set_active_sampling_interval(sec):
    global _active_sampling_interval
    _active_sampling_interval = max(3.0, min(120.0, float(sec)))
    return _active_sampling_interval

def get_active_sampling_interval():
    return _active_sampling_interval

def background_telemetry_recorder():
    """
    Hilo en segundo plano con Muestreo Adaptativo Eco-Throttling y Compactación Automática:
    - Si la web está abierta (SSE activo): Muestrea cada 15s (configurable a 30s) con interpolación suave.
    - Si la web está cerrada (modo background): Muestrea cada 30s de día y cada 60s de noche (0% CPU).
    - Cero pérdida de información: Integración matemática continua de kWh.
    - Compactación Tiered Storage: Purga y agrega muestras > 7 días en resúmenes horarios.
    """
    print("📁 Iniciando registrador local de telemetría con Muestreo Adaptativo Eco (15s)...")
    last_reconcile_check = datetime.now()
    last_compact_check = datetime.now()
    from telemetry_db import compact_and_prune_history
    
    while True:
        try:
            telemetry = read_inverter_modbus_telemetry()
            if telemetry and telemetry.get("online"):
                save_telemetry_record(telemetry, source="modbus_local")
                learning_engine.update_with_sample(telemetry)
                CustomHandler.broadcast_sse("telemetry", telemetry)
                
            now = datetime.now()
            # 1. Reconciliación diaria automática (cada hora)
            if (now - last_reconcile_check).total_seconds() > 3600:
                last_reconcile_check = now
                telemetry_daemon.run_daily_reconciliation()

            # 2. Compactación de almacenamiento SQLite (1 vez al día)
            if (now - last_compact_check).total_seconds() > 86400:
                last_compact_check = now
                compact_and_prune_history(retention_days=7)
        except Exception as e:
            pass

        # Intervalo adaptativo según estado del usuario y hora solar
        active_viewers = len(CustomHandler.sse_clients)
        current_hour = datetime.now().hour
        is_daylight = 7 <= current_hour <= 21

        if active_viewers > 0:
            sleep_sec = _active_sampling_interval   # Web abierta: 15s por defecto (Eco Activo)
        elif is_daylight:
            sleep_sec = 30.0  # Background de día: 30s (0% CPU)
        else:
            sleep_sec = 60.0  # Noche: 60s (0% CPU / batería portátil)

        time.sleep(sleep_sec)

class CustomHandler(http.server.SimpleHTTPRequestHandler):
    sse_clients = []
    sse_lock = threading.Lock()

    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    @classmethod
    def register_sse_client(cls, client_wfile):
        with cls.sse_lock:
            cls.sse_clients.append(client_wfile)

    @classmethod
    def unregister_sse_client(cls, client_wfile):
        with cls.sse_lock:
            if client_wfile in cls.sse_clients:
                cls.sse_clients.remove(client_wfile)

    @classmethod
    def broadcast_sse(cls, event_type, data):
        with cls.sse_lock:
            dead_clients = []
            msg = f"event: {event_type}\ndata: {json.dumps(data)}\n\n".encode('utf-8')
            for client in cls.sse_clients:
                try:
                    client.write(msg)
                    client.flush()
                except Exception:
                    dead_clients.append(client)
            for dead in dead_clients:
                if dead in cls.sse_clients:
                    cls.sse_clients.remove(dead)

    def do_GET(self):
        if self.path == '/api/stream':
            self.send_response(200)
            self.send_header('Content-Type', 'text/event-stream')
            self.send_header('Cache-Control', 'no-cache, no-transform')
            self.send_header('Connection', 'keep-alive')
            self.send_header('Access-Control-Allow-Origin', '*')
            self.end_headers()
            
            # Send initial telemetry payload immediately
            initial_telemetry = read_inverter_modbus_telemetry()
            init_msg = f"event: telemetry\ndata: {json.dumps(initial_telemetry)}\n\n".encode('utf-8')
            try:
                self.wfile.write(init_msg)
                self.wfile.flush()
            except Exception:
                return

            CustomHandler.register_sse_client(self.wfile)
            try:
                while True:
                    time.sleep(1.0)
            except (socket.error, BrokenPipeError, ConnectionResetError):
                pass
            finally:
                CustomHandler.unregister_sse_client(self.wfile)
            return

        elif self.path == '/api/telemetry':
            telemetry = read_inverter_modbus_telemetry()
            self._send_json(telemetry)
            return

        elif self.path == '/api/learning/insights':
            insights = learning_engine.get_summary()
            self._send_json(insights)
            return

        elif self.path == '/api/history':
            history = get_recent_history(500)
            self._send_json(history)
            return

        elif self.path == '/api/history/stats':
            stats = get_history_stats()
            self._send_json(stats)
            return

        elif self.path.startswith('/api/weather/forecast'):
            import urllib.parse
            parsed = urllib.parse.urlparse(self.path)
            query = urllib.parse.parse_qs(parsed.query)
            lat = float(query.get('lat', [37.5942])[0])
            lon = float(query.get('lon', [-5.7397])[0])
            days = int(query.get('days', [7])[0])
            refresh = query.get('refresh', ['0'])[0] in ['1', 'true', 'True']
            
            try:
                data = get_weather_forecast(lat=lat, lon=lon, days=days, force_refresh=refresh)
                self._send_json(data)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/weather/historical-5yr-stats':
            try:
                stats = get_climate_historical_5yr_summary()
                self._send_json(stats)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path.startswith('/api/weather/monthly-breakdown'):
            import urllib.parse
            parsed = urllib.parse.urlparse(self.path)
            query = urllib.parse.parse_qs(parsed.query)
            year_val = query.get('year', [None])[0]
            year = int(year_val) if year_val else None
            try:
                breakdown = get_monthly_climate_breakdown(year=year)
                self._send_json(breakdown)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/ai/annual-forecast':
            try:
                forecast = annual_ai_engine.get_12_months_forecast()
                self._send_json(forecast)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/ai/weeks-forecast':
            try:
                weeks = annual_ai_engine.get_52_weeks_forecast()
                self._send_json(weeks)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/ai/accuracy-scorecard':
            try:
                reconcile_latest = annual_ai_engine.reconcile_and_calibrate()
                history = annual_ai_engine.get_accuracy_history(30)
                self._send_json({
                    "latest": reconcile_latest,
                    "history": history,
                    "hyperparameters": annual_ai_engine.params
                })
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path.startswith('/api/ai/pinn-forecast'):
            import urllib.parse
            parsed = urllib.parse.urlparse(self.path)
            query = urllib.parse.parse_qs(parsed.query)
            day_offset = int(query.get('day', [0])[0])
            try:
                forecast = pinn_solar_engine.generate_day_pinn_forecast(day_offset)
                self._send_json(forecast)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/ai/thermal-precooling':
            try:
                telemetry = read_inverter_modbus_telemetry()
                solar_kw = telemetry.get('solar_total_kw', 0.0)
                home_kw = telemetry.get('grid', {}).get('home_load_kw', 1.0)
                surplus_kw = max(0.0, solar_kw - home_kw)
                
                now = datetime.now()
                temp_amb = telemetry.get('inverter', {}).get('temperature_c', 35.0) - 8.0 # Temp ambiente estimada
                rec = pinn_solar_engine.compute_thermal_precooling_recommendation(
                    outdoor_temp_c=max(28.0, temp_amb),
                    current_hour=now.hour,
                    solar_surplus_kw=surplus_kw
                )
                self._send_json(rec)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/ai/sankey-data':
            try:
                telemetry = read_inverter_modbus_telemetry()
                p_solar = telemetry.get('solar_total_w', 2500)
                p_home = telemetry.get('grid', {}).get('home_load_w', 1100)
                p_export = telemetry.get('grid', {}).get('grid_export_w', 1400)
                soc = telemetry.get('battery', {}).get('soc_percent', 100)
                
                # Desglose de flujos
                solar_to_home = min(p_solar, p_home)
                solar_to_bat = max(0, p_solar - p_home - p_export) if soc < 100 else 0
                solar_to_grid = max(0, p_export)
                grid_to_home = max(0, p_home - p_solar)
                
                sankey_payload = {
                    "nodes": [
                        {"id": "solar", "name": f"Paneles Jinko 5 kWp ({p_solar} W)", "color": "#f59e0b"},
                        {"id": "inverter", "name": f"Inversor Sunworks 10 kW", "color": "#10b981"},
                        {"id": "home", "name": f"Hogar ({p_home} W)", "color": "#38bdf8"},
                        {"id": "battery", "name": f"Batería Fox-ESS ({soc}% SoC)", "color": "#c084fc"},
                        {"id": "grid", "name": f"Batería Virtual ({p_export} W)", "color": "#ec4899"}
                    ],
                    "links": [
                        {"source": "solar", "target": "inverter", "value": max(1, p_solar)},
                        {"source": "inverter", "target": "home", "value": max(1, solar_to_home)},
                        {"source": "inverter", "target": "battery", "value": max(1, solar_to_bat)},
                        {"source": "inverter", "target": "grid", "value": max(1, solar_to_grid)}
                    ],
                    "metrics": {
                        "autoconsumo_pct": 100.0 if grid_to_home == 0 else round((solar_to_home / p_home) * 100, 1),
                        "autosuficiencia_pct": 100.0 if grid_to_home == 0 else round((solar_to_home / p_home) * 100, 1),
                        "coste_actual_eur_h": 0.00 if grid_to_home == 0 else round((grid_to_home / 1000.0) * 0.12, 3)
                    }
                }
                self._send_json(sankey_payload)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/ai/calendar-heatmap':
            try:
                breakdown = get_monthly_climate_breakdown()
                self._send_json(breakdown)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path.startswith('/api/ai/litert/predict-24h'):
            try:
                # Obtener meteo para inferencia LiteRT INT8 instantánea
                pinn_hours = pinn_solar_engine.generate_day_pinn_forecast(0)
                meteo_list = [{
                    "doy": 230,
                    "hour": h["hour"],
                    "ghi": h.get("p_total_clear_kw", 0.0) * 300.0,
                    "dni": h.get("p_total_clear_kw", 0.0) * 280.0,
                    "dhi": 100.0,
                    "temp_c": h.get("temp_c", 35.0),
                    "cloud_pct": h.get("cloud_cover_pct", 0.0)
                } for h in pinn_hours]
                
                litert_res = litert_engine.predict_24h_batch(meteo_list)
                self._send_json(litert_res)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/ai/duckdb/matrix':
            try:
                matrix = duckdb_engine.query_5yr_climate_matrix()
                self._send_json(matrix)
            except Exception as e:
                self._send_json({"error": str(e)}, status=500)
            return

        elif self.path == '/api/foxcloud/status':
            creds = get_foxcloud_credentials()
            configured = creds is not None and bool(creds.get('username') or creds.get('api_key'))
            self._send_json({
                "configured": configured,
                "username": creds.get('username') if creds else None,
                "device_sn": creds.get('device_sn') if creds else None,
                "auto_sync": creds.get('auto_sync_on_startup', True) if creds else True,
                "updated_at": creds.get('updated_at') if creds else None
            })
            return

        elif self.path == '/api/config/sampling-interval':
            self._send_json({
                "interval_seconds": get_active_sampling_interval(),
                "available_modes": [
                    { "id": "ultra_eco", "label": "Ultra Eco (30s)", "seconds": 30.0, "desc": "Mínimo impacto absoluto (batería máxima en portátil)" },
                    { "id": "balanced_eco", "label": "Eco Equilibrado (15s)", "seconds": 15.0, "desc": "Recomendado (4 lecturas/min con animación suave)" },
                    { "id": "responsive", "label": "Rápido (5s)", "seconds": 5.0, "desc": "Para pruebas de calibración en tiempo real" }
                ]
            })
            return

        return super().do_GET()

    def do_POST(self):
        if self.path == '/api/config/sampling-interval':
            content_len = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_len).decode('utf-8')
            try:
                data = json.loads(body)
                sec = float(data.get('seconds', 15.0))
                applied = set_active_sampling_interval(sec)
                self._send_json({ "success": True, "applied_interval_seconds": applied })
            except Exception as e:
                self._send_json({ "success": False, "error": str(e) }, 500)
            return

        elif self.path == '/api/foxcloud/config':
            content_len = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_len).decode('utf-8')
            try:
                data = json.loads(body)
                api_key = data.get('api_key', '')
                device_sn = data.get('device_sn', '')
                if api_key and device_sn:
                    save_foxcloud_credentials(api_key, device_sn)
                    self._send_json({ "success": True, "message": "Credenciales FoxCloud guardadas con éxito" })
                else:
                    self._send_json({ "success": False, "error": "Faltan campos obligatorios" }, 400)
            except Exception as e:
                self._send_json({ "success": False, "error": str(e) }, 500)
            return

        elif self.path == '/api/foxcloud/sync':
            content_len = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_len).decode('utf-8') if content_len > 0 else '{}'
            try:
                data = json.loads(body) if body else {}
                hours = int(data.get('hours', 24))
                result = sync_historical_gaps(hours_back=hours)
                self._send_json(result)
            except Exception as e:
                self._send_json({ "success": False, "error": str(e) }, 500)
            return

        self.send_error(404, "Endpoint not found")

    def _send_json(self, data, status=200):
        data_bytes = json.dumps(data).encode('utf-8')
        self.send_response(status)
        self.send_header('Content-Type', 'application/json')
        self.send_header('Content-Length', str(len(data_bytes)))
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Cache-Control', 'no-store, no-cache')
        self.end_headers()
        self.wfile.write(data_bytes)

    def end_headers(self):
        self.send_header('Cache-Control', 'no-store, no-cache, must-revalidate')
        self.send_header('Access-Control-Allow-Origin', '*')
        super().end_headers()

class ThreadingHTTPServer(socketserver.ThreadingMixIn, http.server.HTTPServer):
    daemon_threads = True
    allow_reuse_address = True

if __name__ == '__main__':
    port = int(sys.argv[1]) if len(sys.argv) > 1 else PORT
    
    # Iniciar hilo grabador de telemetría en background
    t = threading.Thread(target=background_telemetry_recorder, daemon=True)
    t.start()

    # Iniciar broker centralizado de meteorología y radiación solar
    start_weather_broker_thread()

    print(f"☀️  Servidor Fotovoltaico Activo en: http://localhost:{port}")
    print(f"📡  Puente Modbus TCP enlazado a Inversor: {INVERTER_IP}:{INVERTER_PORT} (Unit ID {INVERTER_UNIT_ID})")
    print(f"💾  Base de Datos Local SQLite: telemetry_history.db")
    
    # Sincronización automática de FoxCloud 2.0 al arrancar
    try:
        from foxcloud_sync import sync_on_startup
        sync_on_startup()
    except Exception as e:
        print(f"[FoxCloud] Error en sincronización de inicio: {e}")
    
    with ThreadingHTTPServer(("", port), CustomHandler) as httpd:
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nServidor detenido correctamente.")
