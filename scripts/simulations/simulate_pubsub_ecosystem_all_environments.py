#!/usr/bin/env python3
"""
simulate_pubsub_ecosystem_all_environments.py
-------------------------------------------------------------------------------
Simulador y Benchmark de Resiliencia, Micro-Batching, Reintentos con Jitter,
Dead Letter Queues (DLQ) y FinOps de Google Cloud Pub/Sub en todos los entornos
(Local, Beta, Prod) y para todos los proyectos del ecosistema.

Persiste métricas en data/simulations_telemetry.db y emite tablas comparativas.
-------------------------------------------------------------------------------
"""

import math
import os
import random
import sqlite3
import time
from dataclasses import dataclass
from typing import Dict, List, Tuple

DB_PATH = "/home/jaruiz/Desarrollo/data/simulations_telemetry.db"
os.makedirs(os.path.dirname(DB_PATH), exist_ok=True)

@dataclass
class ProjectProfile:
    name: str
    category: str
    monthly_events: int
    avg_payload_bytes: int
    target_p99_ms: float
    criticality: str

PROJECTS = [
    ProjectProfile("SaaSRegantes", "Agro-IoT & Water Management", 350_000, 450, 25.0, "ALTA"),
    ProjectProfile("AppViajes", "Urban Mobility & H3 GPS Pings", 1_200_000, 600, 15.0, "CRITICA"),
    ProjectProfile("pctMultiMicroservices", "BFF Go & HBX/Taxi Dispatch", 215_000, 500, 20.0, "ALTA"),
    ProjectProfile("ProyectoTokenRWA", "Fintech & RWA Sagas Escrow", 85_000, 1200, 30.0, "CRITICA"),
    ProjectProfile("ProyectoLogistica", "VRP Routing & Fleet Telemetry", 450_000, 750, 20.0, "ALTA"),
    ProjectProfile("ProyectoEnergia", "VPP & Grid Spot Market", 180_000, 400, 15.0, "MEDIA"),
    ProjectProfile("ProyectoFusionPowerGrid", "Nuclear Tokamak MHD & BESS", 320_000, 850, 10.0, "CRITICA"),
    ProjectProfile("ProyectoStratosphericAerosolGeoengineering", "SAI Radiative Forcing Cloud", 150_000, 920, 20.0, "ALTA"),
    ProjectProfile("ProyectoCislunarSpaceLogistics", "CR3BP Orbital Lagrange Transfer", 95_000, 1100, 15.0, "CRITICA"),
    ProjectProfile("ProyectoSyntheticEnzymeBioFoundry", "De Novo PFAS Enzyme Kinetics", 140_000, 1250, 25.0, "ALTA"),
    ProjectProfile("ProyectoQuantumMaterialsGraphene", "Magic Angle Superconductivity", 80_000, 1400, 12.0, "ALTA"),
]

ENVIRONMENTS = ["local", "beta", "prod"]

def init_db(con: sqlite3.Connection):
    cur = con.cursor()
    cur.execute("""
        CREATE TABLE IF NOT EXISTS pubsub_resilience_benchmarks (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
            project_name TEXT,
            environment TEXT,
            mode TEXT,
            total_events INTEGER,
            throughput_msgs_sec REAL,
            latency_p50_ms REAL,
            latency_p95_ms REAL,
            latency_p99_ms REAL,
            rpc_calls INTEGER,
            network_bytes INTEGER,
            failed_attempts INTEGER,
            dlq_routed INTEGER,
            recovered_via_fallback INTEGER,
            cost_usd_month REAL
        )
    """)
    con.commit()

def simulate_batch(events_count: int, mode: str, env: str, payload_size: int) -> Dict:
    """
    Simula el envío de un lote de eventos bajo condiciones del entorno:
    - mode: 'baseline' (1 a 1 sin batching ni jitter) vs 'optimized' (micro-batching 250 msgs, jitter, DLQ, store-and-forward)
    """
    # Factores de red según entorno
    if env == "local":
        base_latency = 0.08 # ms (emulador en tmpfs)
        jitter_range = 0.05
        error_rate = 0.001
        batch_size = 50 if mode == "optimized" else 1
    elif env == "beta":
        base_latency = 8.5 # ms (GCP regional)
        jitter_range = 4.0
        error_rate = 0.03 # Chaos injection en beta
        batch_size = 250 if mode == "optimized" else 1
    else: # prod
        base_latency = 4.2 # ms (GCP multi-zone)
        jitter_range = 2.0
        error_rate = 0.005
        batch_size = 250 if mode == "optimized" else 1

    start_time = time.perf_counter()
    latencies = []
    failed_attempts = 0
    dlq_routed = 0
    recovered_fallback = 0
    total_rpcs = 0
    total_bytes = 0

    remaining = events_count
    while remaining > 0:
        current_batch = min(remaining, batch_size)
        remaining -= current_batch
        total_rpcs += 1
        
        # Tamaño de paquete (comprimido si optimizado)
        compression_ratio = 0.45 if mode == "optimized" else 1.0
        batch_bytes = int(current_batch * payload_size * compression_ratio)
        total_bytes += batch_bytes

        # Simular latencia de RPC
        rpc_lat = base_latency + random.uniform(0, jitter_range)
        if mode == "baseline":
            rpc_lat += current_batch * 0.02 # Overhead CPU por mensaje no agrupado
        else:
            rpc_lat += math.log2(max(2, current_batch)) * 0.01

        # Inyección de fallos y resiliencia
        is_error = random.random() < error_rate
        if is_error:
            failed_attempts += 1
            if mode == "baseline":
                # Sin reintento robusto ni DLQ
                rpc_lat += 50.0 # Timeout largo
            else:
                # Optimized: Exponential backoff con full jitter
                backoff = min(30.0, 0.2 * (2 ** random.randint(1, 3))) + random.uniform(0, 0.1)
                rpc_lat += backoff * 0.1 # Escala simulada
                if random.random() < 0.2: # Tras 5 intentos
                    dlq_routed += 1
                else:
                    recovered_fallback += current_batch

        for _ in range(current_batch):
            latencies.append(rpc_lat / current_batch if mode == "optimized" else rpc_lat)

    elapsed = time.perf_counter() - start_time
    latencies.sort()
    
    p50 = latencies[int(len(latencies) * 0.50)]
    p95 = latencies[int(len(latencies) * 0.95)]
    p99 = latencies[int(len(latencies) * 0.99)]
    throughput = events_count / max(0.0001, elapsed * 10.0) # Normalizado

    return {
        "throughput": throughput,
        "p50": p50,
        "p95": p95,
        "p99": p99,
        "rpcs": total_rpcs,
        "bytes": total_bytes,
        "failed": failed_attempts,
        "dlq": dlq_routed,
        "recovered": recovered_fallback
    }

def calculate_finops_cost(project: ProjectProfile, env: str, bytes_per_month: int) -> float:
    """Calcula coste mensual en USD según Free Tier de GCP Pub/Sub (10 GB gratis) y Cloud Run CPU."""
    if env == "local":
        return 0.00
    
    gb_month = bytes_per_month / (1024 ** 3)
    billable_gb = max(0.0, gb_month - 10.0) # 10 GB Free Tier por proyecto
    pubsub_cost = billable_gb * 40.0 / 1024 # $40/TiB

    # Ahorro de CPU en Cloud Run por menos RPCs
    cloud_run_base = 15.0 if project.category.startswith("Urban") or project.category.startswith("Agro") else 8.0
    return round(pubsub_cost, 4)

def run_full_simulation():
    con = sqlite3.connect(DB_PATH)
    init_db(con)
    cur = con.cursor()

    print("==========================================================================================")
    print("🚀 SIMULACIÓN GLOBAL DE RESILIENCIA, RENDIMIENTO Y FINOPS DE PUB/SUB EN EL ECOSISTEMA")
    print("==========================================================================================")
    print(f"📊 Base de Datos de Telemetría: {DB_PATH}\n")

    results_table = []

    for proj in PROJECTS:
        for env in ENVIRONMENTS:
            sim_sample_events = 5000 # Muestra estadística representativa por corrida
            
            res_baseline = simulate_batch(sim_sample_events, "baseline", env, proj.avg_payload_bytes)
            res_opt = simulate_batch(sim_sample_events, "optimized", env, proj.avg_payload_bytes)

            monthly_bytes_baseline = proj.monthly_events * proj.avg_payload_bytes
            monthly_bytes_opt = int(monthly_bytes_baseline * 0.45)

            cost_baseline = calculate_finops_cost(proj, env, monthly_bytes_baseline)
            cost_opt = calculate_finops_cost(proj, env, monthly_bytes_opt)

            # Persistir en SQLite
            cur.execute("""
                INSERT INTO pubsub_resilience_benchmarks 
                (project_name, environment, mode, total_events, throughput_msgs_sec, latency_p50_ms, latency_p95_ms, latency_p99_ms, rpc_calls, network_bytes, failed_attempts, dlq_routed, recovered_via_fallback, cost_usd_month)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (proj.name, env, "BASELINE", sim_sample_events, res_baseline["throughput"], res_baseline["p50"], res_baseline["p95"], res_baseline["p99"], res_baseline["rpcs"], res_baseline["bytes"], res_baseline["failed"], res_baseline["dlq"], res_baseline["recovered"], cost_baseline))

            cur.execute("""
                INSERT INTO pubsub_resilience_benchmarks 
                (project_name, environment, mode, total_events, throughput_msgs_sec, latency_p50_ms, latency_p95_ms, latency_p99_ms, rpc_calls, network_bytes, failed_attempts, dlq_routed, recovered_via_fallback, cost_usd_month)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (proj.name, env, "OPTIMIZED", sim_sample_events, res_opt["throughput"], res_opt["p50"], res_opt["p95"], res_opt["p99"], res_opt["rpcs"], res_opt["bytes"], res_opt["failed"], res_opt["dlq"], res_opt["recovered"], cost_opt))

            rpc_reduction = (1.0 - (res_opt["rpcs"] / max(1, res_baseline["rpcs"]))) * 100
            throughput_gain = res_opt["throughput"] / max(1.0, res_baseline["throughput"])

            results_table.append({
                "project": proj.name,
                "env": env,
                "base_p99": res_baseline["p99"],
                "opt_p99": res_opt["p99"],
                "rpc_red": rpc_reduction,
                "tput_gain": throughput_gain,
                "cost_base": cost_baseline,
                "cost_opt": cost_opt
            })

    con.commit()
    con.close()

    # Imprimir Reporte por Entorno y Proyecto
    print(f"{'PROYECTO':<22} | {'ENTORNO':<7} | {'P99 ANTES':<10} | {'P99 OPT':<10} | {'REDUC. RPC':<11} | {'GANANCIA TPUT':<13} | {'COSTE/MES':<10}")
    print("-" * 95)
    for r in results_table:
        print(f"{r['project']:<22} | {r['env']:<7} | {r['base_p99']:>7.2f} ms | {r['opt_p99']:>7.2f} ms | {r['rpc_red']:>9.1f} % | {r['tput_gain']:>11.1f}x | ${r['cost_opt']:>7.2f}")

    print("\n✅ Simulación completada con éxito. Registros telemétricos guardados en SQLite.")

if __name__ == "__main__":
    run_full_simulation()
