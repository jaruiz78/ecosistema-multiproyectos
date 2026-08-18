"""
Universal Weather Broker Proxy for MultiProyectos Scripts and AI Pipelines
Provides 5-year ERA5 climate reanalysis, PINN solar models, quantile forecasting,
LiteRT INT8 tensor execution, DuckDB in-process vectorized analytics, and online self-learning.
"""
import sys
import os

APP_SOLAR_DIR = "/home/jaruiz/Desarrollo/apps/ProyectoSolarTocina"
if APP_SOLAR_DIR not in sys.path:
    sys.path.insert(0, APP_SOLAR_DIR)

from weather_broker import (
    get_weather_forecast,
    fetch_open_meteo_live,
    init_weather_db,
    get_climate_historical_5yr_summary,
    get_monthly_climate_breakdown
)
from pinn_solar_model import pinn_solar_engine
from annual_ai_predictor import annual_ai_engine
from telemetry_ingestor_daemon import telemetry_daemon
from litert_solar_kernel import litert_engine
from duckdb_analytics_engine import duckdb_engine

__all__ = [
    "get_weather_forecast",
    "fetch_open_meteo_live",
    "init_weather_db",
    "get_climate_historical_5yr_summary",
    "get_monthly_climate_breakdown",
    "pinn_solar_engine",
    "annual_ai_engine",
    "telemetry_daemon",
    "litert_engine",
    "duckdb_engine"
]
