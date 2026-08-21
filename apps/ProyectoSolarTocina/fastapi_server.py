#!/usr/bin/env python3
"""
fastapi_server.py
=================
Servidor de Alto Rendimiento ASGI / FastAPI para el Gemelo Digital Solar Tocina.
- WebSockets de telemetría de ultra-baja latencia (/ws/stream).
- Endpoints totalmente asíncronos y no bloqueantes.
- Validación Pydantic v2 y documentación interactiva OpenAPI (/docs).
- Servidor de ficheros estáticos integrado para Dashboard PWA (src/).
"""

import os
import json
import asyncio
from typing import Dict, List, Any, Optional
from datetime import datetime

from fastapi import FastAPI, WebSocket, WebSocketDisconnect, Query, Request, Response
from fastapi.responses import JSONResponse, FileResponse, HTMLResponse, StreamingResponse
from fastapi.staticfiles import StaticFiles
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field

# Motores y DAOs
from telemetry_db import save_telemetry_record, get_recent_history, get_history_stats, get_today_hourly_telemetry, get_today_high_res_telemetry
from historical_analytics_service import get_multidimensional_history
from duckdb_analytics_engine import duckdb_engine
from weather_broker import (
    get_weather_forecast,
    get_climate_historical_5yr_summary,
    get_monthly_climate_breakdown,
    get_radar_layers,
    get_current_weather_summary,
    get_solar_nowcast_minutely
)
from online_learning_twin import learning_engine
from annual_ai_predictor import annual_ai_engine
from pinn_solar_model import pinn_solar_engine
from litert_solar_kernel import litert_engine
from daikin_iot_automation import daikin_iot_engine
from smart_plugs_manager import smart_plugs_engine
from environmental_sensors_manager import environmental_sensors_engine
from naturgy_virtual_battery_controller import naturgy_vb_engine
from soiling_detector import SoilingDetector
from backup_manager import BackupManager
from server import get_current_telemetry_with_ev, _cached_telemetry

app = FastAPI(
    title="Gemelo Digital Solar Tocina API",
    description="Backend Fotovoltaico Asíncrono de Alta Eficiencia (FastAPI + DuckDB + Numba + WebSockets)",
    version="6.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

SRC_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "src")

# -------------------------------------------------------------
# PYDANTIC SCHEMAS
# -------------------------------------------------------------
class OmodaSetSocRequest(BaseModel):
    soc_percent: float = Field(..., ge=0.0, le=100.0, description="Nivel de batería del Omoda 7 PHEV (%)")

class EnvironmentalSensorRecord(BaseModel):
    sensor_id: str
    temperature_c: float
    humidity_pct: float

class SmartPlugToggleRequest(BaseModel):
    plug_id: str
    state: bool

# -------------------------------------------------------------
# WEBSOCKET MANAGER
# -------------------------------------------------------------
class ConnectionManager:
    def __init__(self):
        self.active_connections: List[WebSocket] = []

    async def connect(self, websocket: WebSocket):
        await websocket.accept()
        self.active_connections.append(websocket)

    def disconnect(self, websocket: WebSocket):
        if websocket in self.active_connections:
            self.active_connections.remove(websocket)

    async def broadcast(self, message: str):
        for connection in list(self.active_connections):
            try:
                await connection.send_text(message)
            except Exception:
                self.disconnect(connection)

ws_manager = ConnectionManager()

# Background broadcast task
async def telemetry_broadcast_loop():
    while True:
        try:
            telemetry = get_current_telemetry_with_ev()
            payload = json.dumps({"event": "telemetry", "data": telemetry})
            await ws_manager.broadcast(payload)
        except Exception:
            pass
        await asyncio.sleep(2.5)

@app.on_event("startup")
async def startup_event():
    asyncio.create_task(telemetry_broadcast_loop())

@app.websocket("/ws/stream")
async def websocket_endpoint(websocket: WebSocket):
    await ws_manager.connect(websocket)
    try:
        # Enviar telemetría inicial
        init_telem = get_current_telemetry_with_ev()
        await websocket.send_text(json.dumps({"event": "telemetry", "data": init_telem}))
        while True:
            # Mantener conexión viva
            data = await websocket.receive_text()
    except WebSocketDisconnect:
        ws_manager.disconnect(websocket)
    except Exception:
        ws_manager.disconnect(websocket)

# -------------------------------------------------------------
# REST ENDPOINTS
# -------------------------------------------------------------
@app.get("/api/telemetry")
async def api_telemetry():
    return get_current_telemetry_with_ev()

@app.get("/api/stream")
async def sse_stream():
    async def event_generator():
        while True:
            telem = get_current_telemetry_with_ev()
            yield f"event: telemetry\ndata: {json.dumps(telem)}\n\n"
            await asyncio.sleep(3.0)
    return StreamingResponse(event_generator(), media_type="text/event-stream")

@app.get("/api/history")
async def api_history(limit: int = 500):
    return get_recent_history(limit)

@app.get("/api/history/today-hourly")
async def api_today_hourly(date: Optional[str] = None):
    return get_today_hourly_telemetry(date)

@app.get("/api/history/today-high-res")
async def api_today_high_res(date: Optional[str] = None):
    return get_today_high_res_telemetry(date)

@app.get("/api/history/analytics")
async def api_history_analytics(granularity: str = "month", year: int = 2026, month: int = 8, date: str = ""):
    return get_multidimensional_history(granularity=granularity, year=year, month=month, date_str=date)

@app.get("/api/history/stats")
async def api_history_stats():
    return get_history_stats()

@app.get("/api/weather/current")
async def api_weather_current():
    return get_current_weather_summary()

@app.get("/api/weather/forecast")
async def api_weather_forecast(days: int = 7):
    return get_weather_forecast(days)

@app.get("/api/weather/radar-layers")
async def api_weather_radar():
    return get_radar_layers()

@app.get("/api/weather/nowcast-minutely")
async def api_weather_nowcast():
    return get_solar_nowcast_minutely()

@app.get("/api/weather/historical-5yr-stats")
async def api_weather_5yr():
    return get_climate_historical_5yr_summary()

@app.get("/api/weather/monthly-breakdown")
async def api_weather_monthly(month: int = 8):
    return get_monthly_climate_breakdown(month)

@app.get("/api/ai/annual-forecast")
async def api_ai_annual():
    return annual_ai_engine.get_annual_forecast_breakdown()

@app.get("/api/ai/duckdb/matrix")
async def api_duckdb_matrix():
    return {
        "status": "success",
        "duckdb_5yr_matrix": duckdb_engine.query_5yr_climate_matrix(),
        "recent_density": duckdb_engine.query_recent_telemetry_density(7)
    }

@app.post("/api/ai/duckdb/export-parquet")
async def api_duckdb_export_parquet():
    return duckdb_engine.export_to_parquet()

@app.get("/api/ai/thermal-precooling")
async def api_thermal_precooling():
    from server import CustomHandler
    # Call the logic via engine
    from environmental_sensors_manager import environmental_sensors_engine
    return {
        "precooling_active": False,
        "recommendation": "Persianas cerradas en horas punta. Free-cooling nocturno activo.",
        "environmental_sensors": environmental_sensors_engine.get_full_status()
    }

@app.get("/api/environmental-sensors/status")
async def api_sensors_status():
    return environmental_sensors_engine.get_full_status()

@app.get("/api/daikin/iot/status")
async def api_daikin_status():
    return daikin_iot_engine.get_full_status()

@app.get("/api/smart-plugs/status")
async def api_plugs_status():
    return smart_plugs_engine.get_full_status()

@app.get("/api/naturgy/virtual-battery/status")
async def api_vb_status():
    return naturgy_vb_engine.get_status()

@app.get("/api/learning/insights")
async def api_learning_insights():
    return learning_engine.get_summary()

# Mount Static Files (PWA Dashboard)
if os.path.exists(SRC_DIR):
    app.mount("/", StaticFiles(directory=SRC_DIR, html=True), name="static")

if __name__ == "__main__":
    import uvicorn
    print("🚀 Iniciando Servidor Fotovoltaico ASGI (FastAPI + Uvicorn) en http://0.0.0.0:8526")
    uvicorn.run(app, host="0.0.0.0", port=8526, log_level="warning")
