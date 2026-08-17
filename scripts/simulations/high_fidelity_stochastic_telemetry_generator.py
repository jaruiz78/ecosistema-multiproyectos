#!/usr/bin/env python3
"""
high_fidelity_stochastic_telemetry_generator.py
=============================================================================
GENERADOR DE TELEMETRÍA ESTOCÁSTICA DE ALTA FIDELIDAD Y RUIDO NO-GAUSSIANO
Gemelo Digital Unificado (CMU / Stanford / Princeton IAS Benchmark)
-----------------------------------------------------------------------------
Modelos Matemáticos Implementados:
1. Proceso de Ornstein-Uhlenbeck con Saltos de Lévy (Jump-Diffusion):
   dX_t = theta * (mu - X_t) * dt + sigma * dW_t + J_t * dN_t
2. Ruido de Cola Pesada (Student-t, gl=3 / Cauchy) para sensores IoT y GPS.
3. Cadena de Markov para Estados de Cobertura de Red (4G/5G/Starlink/Túnel).
4. Deriva Temporal de Sensores (Brownian Sensor Drift).
5. Persistencia eficiente en SQLite (simulations_telemetry.db).
=============================================================================
"""
import os
import sys
import time
import math
import sqlite3
import argparse
from pathlib import Path
from dataclasses import dataclass, asdict
from typing import List, Dict, Tuple, Generator
import numpy as np

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
DB_PATH = WORKSPACE_ROOT / "data" / "simulations_telemetry.db"

@dataclass
class NetworkState:
    LOS = 0          # Line of Sight (Excelente: latencia 15-25ms, packet_loss 0.001)
    URBAN_CANYON = 1 # Cañón Urbano (Multi-path: latencia 60-180ms, packet_loss 0.05)
    DEGRADED = 2     # Cobertura Débil (latencia 200-800ms, packet_loss 0.20)
    BLACKOUT = 3     # Corte Total / Túnel (latencia inf, packet_loss 1.00)

class HighFidelityStochasticTelemetryGenerator:
    """
    Genera streams de telemetría de ultra-alta fidelidad física para sensores IoT,
    vehículos H3, redes eléctricas VPP y cadenas de frío.
    """

    def __init__(self, seed: int = 42):
        np.random.seed(seed)
        self.transition_matrix = np.array([
            # LOS, URBAN, DEGRADED, BLACKOUT
            [0.85, 0.10, 0.04, 0.01],  # from LOS
            [0.15, 0.70, 0.12, 0.03],  # from URBAN
            [0.10, 0.20, 0.60, 0.10],  # from DEGRADED
            [0.30, 0.20, 0.20, 0.30]   # from BLACKOUT
        ])
        self._ensure_tables()

    def _ensure_tables(self):
        """Crea tablas especializadas para telemetría estocástica si no existen."""
        with sqlite3.connect(DB_PATH) as conn:
            c = conn.cursor()
            c.execute("""
                CREATE TABLE IF NOT EXISTS high_fidelity_sensor_telemetry (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    timestamp REAL,
                    entity_id TEXT,
                    vertical_domain TEXT,
                    h3_index TEXT,
                    true_value REAL,
                    noisy_reading REAL,
                    sensor_drift REAL,
                    network_state INTEGER,
                    network_latency_ms REAL,
                    packet_dropped INTEGER,
                    snr_db REAL
                )
            """)
            c.execute("CREATE INDEX IF NOT EXISTS idx_hf_sensor_entity ON high_fidelity_sensor_telemetry(entity_id)")
            c.execute("CREATE INDEX IF NOT EXISTS idx_hf_sensor_time ON high_fidelity_sensor_telemetry(timestamp)")
            conn.commit()

    def simulate_ornstein_uhlenbeck_with_jumps(
        self,
        n_steps: int,
        dt: float = 0.1,
        theta: float = 1.2,
        mu: float = 25.0,
        sigma: float = 1.5,
        jump_lambda: float = 0.05,
        jump_scale: float = 4.0
    ) -> np.ndarray:
        """
        Simula una trayectoria de Ornstein-Uhlenbeck con saltos no gaussianos (Student-t).
        """
        X = np.zeros(n_steps)
        X[0] = mu + np.random.normal(0, sigma)
        
        # Deriva browniana de calibración del sensor
        sensor_drift = np.cumsum(np.random.normal(0, 0.005, n_steps))
        
        for t in range(1, n_steps):
            # Término de reversión a la media
            drift = theta * (mu - X[t-1]) * dt
            # Difusión browniana estándar
            diffusion = sigma * np.sqrt(dt) * np.random.normal(0, 1)
            # Proceso de Poisson de saltos
            n_jumps = np.random.poisson(jump_lambda * dt)
            # Saltos con distribución de Student-t (cola pesada, gl=3)
            jump = np.sum(np.random.standard_t(df=3, size=n_jumps) * jump_scale) if n_jumps > 0 else 0.0
            
            X[t] = X[t-1] + drift + diffusion + jump

        return X, sensor_drift

    def simulate_network_channel(self, n_steps: int) -> Tuple[np.ndarray, np.ndarray, np.ndarray]:
        """
        Simula el canal de comunicación móvil mediante una Cadena de Markov.
        Retorna (estados, latencias_ms, paquetes_perdidos).
        """
        states = np.zeros(n_steps, dtype=int)
        latencies = np.zeros(n_steps)
        drops = np.zeros(n_steps, dtype=int)
        
        current_state = NetworkState.LOS
        
        for t in range(n_steps):
            # Transición de estado Markov
            current_state = np.random.choice(4, p=self.transition_matrix[current_state])
            states[t] = current_state
            
            if current_state == NetworkState.LOS:
                latencies[t] = np.random.gamma(shape=4.0, scale=4.0) + 12.0 # ~28ms
                drops[t] = 1 if np.random.rand() < 0.002 else 0
            elif current_state == NetworkState.URBAN_CANYON:
                latencies[t] = np.random.gamma(shape=6.0, scale=15.0) + 40.0 # ~130ms
                drops[t] = 1 if np.random.rand() < 0.05 else 0
            elif current_state == NetworkState.DEGRADED:
                latencies[t] = np.random.gamma(shape=8.0, scale=40.0) + 150.0 # ~470ms
                drops[t] = 1 if np.random.rand() < 0.22 else 0
            else: # BLACKOUT
                latencies[t] = 9999.0 # Timeout
                drops[t] = 1

        return states, latencies, drops

    def generate_and_persist_batch(
        self,
        entity_id: str,
        vertical_domain: str,
        n_points: int = 1000,
        base_h3: str = "88390cb653fffff"
    ) -> Dict[str, float]:
        """
        Genera un dataset sintético de alta fidelidad y lo persiste en SQLite.
        """
        t0 = time.time()
        
        true_values, drifts = self.simulate_ornstein_uhlenbeck_with_jumps(n_points)
        net_states, net_latencies, net_drops = self.simulate_network_channel(n_points)
        
        # Ruido de medición en sensor: Gaussiano + Ruido Cauchy (outliers esporádicos)
        gaussian_noise = np.random.normal(0, 0.4, n_points)
        cauchy_noise = np.random.standard_cauchy(n_points) * 0.05
        noisy_readings = true_values + drifts + gaussian_noise + cauchy_noise
        
        records = []
        now = time.time()
        
        for i in range(n_points):
            snr = 20.0 * np.log10(max(abs(true_values[i]), 1e-4) / max(abs(gaussian_noise[i] + cauchy_noise[i]), 1e-4))
            records.append((
                now + i * 0.5,
                entity_id,
                vertical_domain,
                base_h3,
                float(true_values[i]),
                float(noisy_readings[i]),
                float(drifts[i]),
                int(net_states[i]),
                float(net_latencies[i]),
                int(net_drops[i]),
                float(snr)
            ))

        with sqlite3.connect(DB_PATH) as conn:
            c = conn.cursor()
            c.executemany("""
                INSERT INTO high_fidelity_sensor_telemetry 
                (timestamp, entity_id, vertical_domain, h3_index, true_value, noisy_reading, 
                 sensor_drift, network_state, network_latency_ms, packet_dropped, snr_db)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, records)
            conn.commit()

        elapsed = time.time() - t0
        drop_rate = np.mean(net_drops) * 100.0
        mean_snr = float(np.mean([r[10] for r in records]))
        
        return {
            "entity_id": entity_id,
            "records_generated": n_points,
            "packet_drop_rate_pct": round(drop_rate, 2),
            "mean_latency_ms": round(float(np.mean(net_latencies[net_drops == 0])), 2) if np.any(net_drops == 0) else 9999.0,
            "mean_snr_db": round(mean_snr, 2),
            "elapsed_sec": round(elapsed, 3)
        }

def run_multi_vertical_stochastic_ingestion(n_entities: int = 10, points_per_entity: int = 500):
    print("🛰️ [High-Fidelity Telemetry] Iniciando generación estocástica no-gaussiana...")
    generator = HighFidelityStochasticTelemetryGenerator()
    
    verticals = [
        ("VPP_INVERTER_01", "ProyectoVPP"),
        ("COLD_CHAIN_SENSOR_04", "ProyectoFleetColdChain"),
        ("WATER_DESAL_PUMP_12", "ProyectoSmartWaterDesal"),
        ("VEHICLE_RADAR_H3_99", "AppViajes"),
        ("HOSPITAL_POWER_GRID", "ProyectoSalud"),
        ("MARITIME_CONTAINER_44", "ProyectoMaritime"),
        ("OT_TURBINE_SCADA", "ProyectoZeroTrustOTMesh"),
        ("AGRI_ROBOT_DRONE_07", "ProyectoAgroBioRobotics"),
        ("PHARMA_VACCINE_BOX", "ProyectoPharmaColdChain"),
        ("AIR_DEFENSE_RADAR_01", "ProyectoDualAirDefense")
    ]
    
    results = []
    for entity_id, vertical in verticals[:n_entities]:
        res = generator.generate_and_persist_batch(entity_id, vertical, n_points=points_per_entity)
        print(f"  ✓ {entity_id} ({vertical}): {res['records_generated']} puntos | Pérdida: {res['packet_drop_rate_pct']}% | Latencia: {res['mean_latency_ms']}ms")
        results.append(res)

    print("✅ [High-Fidelity Telemetry] Ingesta estocástica completada con éxito y persistida en SQLite.")
    return results

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Generador de Telemetría Estocástica de Alta Fidelidad")
    parser.add_argument("--entities", type=int, default=10, help="Número de entidades a simular")
    parser.add_argument("--points", type=int, default=500, help="Puntos por entidad")
    args = parser.parse_args()
    
    run_multi_vertical_stochastic_ingestion(args.entities, args.points)
