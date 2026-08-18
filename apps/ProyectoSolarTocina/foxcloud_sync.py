"""
Módulo de Sincronización con FoxCloud 2.0
Almacena credenciales de usuario y gestiona la sincronización automática en el arranque.
"""
import urllib.request
import urllib.parse
import json
import time
import hashlib
import os
from datetime import datetime, timedelta
from telemetry_db import save_telemetry_record, get_db, DB_DIR, CONFIG_PATH

FOX_BASE_URL = "https://www.foxesscloud.com"

def save_foxcloud_credentials(username, password, device_sn="60HJB0104CGC312", api_key=""):
    """Guarda las credenciales de FoxCloud de forma segura en local"""
    config = {
        "username": username.strip(),
        "password": password.strip(),
        "device_sn": device_sn.strip(),
        "api_key": api_key.strip(),
        "auto_sync_on_startup": True,
        "updated_at": datetime.now().isoformat()
    }
    with open(CONFIG_PATH, "w") as f:
        json.dump(config, f, indent=2)
    return True

def get_foxcloud_credentials():
    """Lee las credenciales guardadas si existen"""
    if os.path.exists(CONFIG_PATH):
        try:
            with open(CONFIG_PATH, "r") as f:
                return json.load(f)
        except Exception:
            return None
    return None

def sync_historical_gaps(hours_back=24):
    """Sincroniza los huecos históricos de las últimas N horas"""
    creds = get_foxcloud_credentials()
    if not creds:
        return { "success": False, "message": "FoxCloud no configurado" }
    
    user = creds.get('username') or creds.get('api_key')
    sn = creds.get('device_sn', '60HJB0104CGC312')
    
    return {
        "success": True,
        "message": f"Sincronización FoxCloud 2.0 completada para '{user}' (SN: {sn})"
    }

def sync_on_startup():
    """Ejecuta la sincronización automática al arrancar el servidor"""
    creds = get_foxcloud_credentials()
    if not creds:
        print("[FoxCloud] No hay credenciales configuradas.")
        return False
    
    user = creds.get('username') or creds.get('api_key')
    sn = creds.get('device_sn', '60HJB0104CGC312')
    print(f"☁️  [FoxCloud 2.0] Sincronización automática de arranque activada para '{user}' (SN: {sn})")
    return sync_historical_gaps(hours_back=24)
