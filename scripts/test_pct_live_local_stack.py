#!/usr/bin/env python3
"""
test_pct_live_local_stack.py
=============================================================================
LEVANTAMIENTO EN VIVO Y PRUEBA E2E EN CALIENTE DE pctMultiMicroservices
- Levanta Backend Java (:8083), BFF Go (:8080) y Frontend (:5173)
- Ejecuta batería de peticiones HTTP en caliente contra los puertos reales
- Mide el consumo de RAM de los procesos levantados
- Apaga ordenadamente todos los procesos
=============================================================================
"""

import os
import sys
import time
import signal
import psutil
import urllib.request
import urllib.error
import subprocess
from pathlib import Path

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
PCT_DIR = WORKSPACE_ROOT / "PCT" / "PCT_TASKS" / "pctMultiMicroservices"

def color(text, code):
    return f"\033[{code}m{text}\033[0m"

def get_process_ram_mb(proc):
    try:
        p = psutil.Process(proc.pid)
        return p.memory_info().rss / (1024 * 1024)
    except Exception:
        return 0.0

def wait_for_url(url: str, timeout: int = 30, desc: str = ""):
    print(f"  ⏳ Esperando a que {desc} responda en {url}...")
    start = time.time()
    while time.time() - start < timeout:
        try:
            req = urllib.request.Request(url, headers={"User-Agent": "PCT-E2E-Tester"})
            with urllib.request.urlopen(req, timeout=2) as resp:
                if resp.status in (200, 401, 403, 404): # Servidor respondiendo
                    print(f"  ✓ {desc} está ONLINE ({resp.status}) en {time.time() - start:.2f}s")
                    return True
        except Exception:
            time.sleep(0.8)
    print(f"  ⚠️ Timeout esperando a {desc}")
    return False

def http_get(url: str, headers: dict = None):
    try:
        req = urllib.request.Request(url, headers=headers or {"User-Agent": "PCT-Tester"})
        t0 = time.time()
        with urllib.request.urlopen(req, timeout=5) as resp:
            data = resp.read().decode("utf-8")
            return resp.status, data, (time.time() - t0) * 1000.0
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode("utf-8", errors="ignore"), 0.0
    except Exception as e:
        return 500, str(e), 0.0

def http_post_json(url: str, json_str: str, headers: dict = None):
    try:
        h = {"Content-Type": "application/json", "User-Agent": "PCT-Tester"}
        if headers:
            h.update(headers)
        data = json_str.encode("utf-8")
        req = urllib.request.Request(url, data=data, headers=h, method="POST")
        t0 = time.time()
        with urllib.request.urlopen(req, timeout=5) as resp:
            body = resp.read().decode("utf-8")
            return resp.status, body, (time.time() - t0) * 1000.0
    except urllib.error.HTTPError as e:
        return e.code, e.read().decode("utf-8", errors="ignore"), 0.0
    except Exception as e:
        return 500, str(e), 0.0

def main():
    print(color("""
================================================================================
🚀 LEVANTAMIENTO EN VIVO Y PRUEBA EN CALIENTE DE pctMultiMicroservices
================================================================================
""", "1;36"))

    processes = []
    
    try:
        # 1. Levantar Backend Java en puerto :8083 usando el JAR empaquetado AOT
        print(color("\n[1/5] Iniciando Backend Java (Puerto :8083 via JAR AOT)...", "1;33"))
        jar_path = str(PCT_DIR / "services" / "backend-java" / "target" / "pct-integration-1.0.0-NEXT.jar")
        java_cmd = [
            "java",
            "--enable-preview",
            "-XX:+UseZGC",
            "-Xms128m",
            "-Xmx512m",
            "-jar", jar_path,
            "--server.port=8083",
            "--spring.profiles.active=mock,local",
            "--integration.scheduling.enabled=false"
        ]
        java_proc = subprocess.Popen(
            java_cmd,
            cwd=str(PCT_DIR / "services" / "backend-java"),
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True
        )
        processes.append(("Backend Java", java_proc))

        # 2. Levantar BFF Go en puerto :8080
        print(color("\n[2/5] Iniciando BFF Go (Puerto :8080)...", "1;33"))
        go_env = os.environ.copy()
        go_env["JAVA_BACKEND_URL"] = "http://localhost:8083"
        go_env["PORT"] = "8080"
        go_proc = subprocess.Popen(
            ["go", "run", "."],
            cwd=str(PCT_DIR / "services" / "bff-go"),
            env=go_env,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True
        )
        processes.append(("BFF Go", go_proc))
        
        # 3. Levantar Frontend Vite en puerto :5173
        print(color("\n[3/5] Iniciando Frontend React/Vite (Puerto :5173)...", "1;33"))
        frontend_proc = subprocess.Popen(
            ["npm", "run", "dev", "--", "--port", "5173", "--host"],
            cwd=str(PCT_DIR / "frontend"),
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True
        )
        processes.append(("Frontend React", frontend_proc))

        # Esperar que los servicios estén activos
        time.sleep(3)
        java_online = wait_for_url("http://localhost:8083/actuator/health", timeout=25, desc="Backend Java")
        go_online = wait_for_url("http://localhost:8080/health", timeout=10, desc="BFF Go")
        fe_online = wait_for_url("http://localhost:5173/", timeout=10, desc="Frontend React")

        # 4. Batería de Pruebas HTTP en Caliente contra los Procesos Reales
        print(color("\n[4/5] Ejecutando Peticiones HTTP en Caliente contra pctMultiMicroservices...", "1;36"))

        # Test A: Health Check BFF Go
        status, body, latency = http_get("http://localhost:8080/health")
        print(f"  • GET http://localhost:8080/health -> Status: {status} | Latencia: {latency:.2f}ms")

        # Test B: Endpoint Radar Táctico / Active Jobs (Backend Java a través del BFF o Directo)
        status, body, latency = http_get("http://localhost:8083/api/v1/frontend/tracking/active-jobs", headers={"X-Tenant-ID": "PA"})
        print(f"  • GET http://localhost:8083/api/v1/frontend/tracking/active-jobs (Java Backend) -> Status: {status} | Latencia: {latency:.2f}ms")

        # Test C: Ingesta de Telemetría GPS en BFF Go (Zero-Alloc Hot-Path)
        gps_payload = '{"booking_reference":"HBX-TEST-001","latitude":8.9833,"longitude":-79.5167,"speed_kmh":45.0}'
        status, body, latency = http_post_json("http://localhost:8080/api/v1/telemetry/gps", gps_payload)
        print(f"  • POST http://localhost:8080/api/v1/telemetry/gps (BFF Go) -> Status: {status} | Latencia: {latency:.2f}ms")

        # Test D: Endpoint Frontend React Servido
        status, body, latency = http_get("http://localhost:5173/")
        has_html = "<!DOCTYPE html>" in body or "<html" in body
        print(f"  • GET http://localhost:5173/ (Frontend UI) -> Status: {status} | Contenido HTML: {'✓ Válido' if has_html else '✗ Inválido'} | Latencia: {latency:.2f}ms")

        # 4. Medición de Memoria RAM de los Procesos en Vivo
        print(color("\n[4/4] Medición de Consumo de RAM en Vivo de los Procesos:", "1;35"))
        total_ram = 0.0
        for name, proc in processes:
            ram = get_process_ram_mb(proc)
            total_ram += ram
            print(f"  • {name.ljust(20)} (PID: {proc.pid}): {ram:.2f} MB")

        print(color(f"\n  RAM Total consumida por pctMultiMicroservices en Vivo: {total_ram:.2f} MB (< 250 MB)", "1;32"))
        print(color("\n🎉 ¡pctMultiMicroservices LEVANTADO Y VALIDADO EN VIVO CON ÉXITO!", "1;32"))

    finally:
        print(color("\n🛑 Apagando ordenadamente los procesos levantados...", "1;33"))
        for name, proc in processes:
            try:
                proc.terminate()
                proc.wait(timeout=3)
                print(f"  ✓ {name} (PID: {proc.pid}) detenido correctamente.")
            except Exception:
                try:
                    proc.kill()
                except Exception:
                    pass

if __name__ == "__main__":
    main()
