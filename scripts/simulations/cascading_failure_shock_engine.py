#!/usr/bin/env python3
"""
cascading_failure_shock_engine.py
=============================================================================
MOTOR DE PROPAGACIÓN DE FALLAS EN CASCADA Y RESILIENCIA SISTÉMICA
Gemelo Digital Unificado (MIT / ETH Zurich / Georgia Tech Benchmark)
-----------------------------------------------------------------------------
Modelos Matemáticos Implementados:
1. Grafo Dirigido Ponderado de Interdependencia de Infraestructuras Críticas (CIP):
   Red Eléctrica (Energía/VPP) -> Cadena de Frío (Pharma/Fleet) -> Salud -> Movilidad (H3).
2. Modelo de Sobrecarga de Motter-Lai y Redistribución Dinámica de Flujos:
   L_i(t) > C_i => Colapso de nodo i y propagación a nodos adyacentes.
3. Índice de Resiliencia Sistémica de Bruneau & Chang:
   R = (1 / T) * integral_{t_0}^{t_0 + T} (Q(t) / Q_0) dt
4. Cálculo del Tiempo Medio de Recuperación (MTTR) y Coste Económico en Cascada.
5. Persistencia telemétrica en SQLite (tabla cascading_resilience_telemetry).
=============================================================================
"""
import os
import sys
import time
import math
import sqlite3
import argparse
from pathlib import Path
from dataclasses import dataclass
from typing import Dict, List, Tuple, Any
import numpy as np

WORKSPACE_ROOT = Path("/home/jaruiz/Desarrollo")
DB_PATH = WORKSPACE_ROOT / "data" / "simulations_telemetry.db"

@dataclass
class SystemicNode:
    name: str
    vertical: str
    capacity: float
    current_load: float
    backup_battery_hours: float
    status: str # "OPERATIONAL" | "DEGRADED" | "FAILED" | "RECOVERING"
    criticality_weight: float

class CascadingFailureShockEngine:
    """
    Simula la dinámica no lineal de propagación de perturbaciones severas
    a través del ecosistema de verticales interconectados.
    """
    def __init__(self):
        self._ensure_tables()
        self.nodes = self._initialize_topology()
        # Matriz de acoplamiento A_ij: fracción de carga transferida de i a j al fallar i
        self.node_names = list(self.nodes.keys())
        self.n = len(self.node_names)
        self.coupling_matrix = self._build_coupling_matrix()

    def _ensure_tables(self):
        with sqlite3.connect(DB_PATH) as conn:
            c = conn.cursor()
            c.execute("""
                CREATE TABLE IF NOT EXISTS cascading_resilience_telemetry (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    timestamp REAL,
                    shock_scenario TEXT,
                    initial_failed_node TEXT,
                    total_nodes INTEGER,
                    cascade_steps INTEGER,
                    nodes_failed_count INTEGER,
                    resilience_index_r REAL,
                    mttr_hours REAL,
                    economic_loss_usd REAL,
                    unserved_energy_mwh REAL,
                    pharma_spoilage_units INTEGER
                )
            """)
            conn.commit()

    def _initialize_topology(self) -> Dict[str, SystemicNode]:
        return {
            "GRID_SUBSTATION": SystemicNode("GRID_SUBSTATION", "ProyectoEnergia", capacity=500.0, current_load=380.0, backup_battery_hours=0.0, status="OPERATIONAL", criticality_weight=1.0),
            "VPP_AGGREGATOR": SystemicNode("VPP_AGGREGATOR", "ProyectoVPP", capacity=250.0, current_load=180.0, backup_battery_hours=4.0, status="OPERATIONAL", criticality_weight=0.9),
            "DESAL_PLANT": SystemicNode("DESAL_PLANT", "ProyectoSmartWaterDesal", capacity=120.0, current_load=95.0, backup_battery_hours=2.0, status="OPERATIONAL", criticality_weight=0.8),
            "PHARMA_DEPOT": SystemicNode("PHARMA_DEPOT", "ProyectoPharmaColdChain", capacity=80.0, current_load=60.0, backup_battery_hours=6.0, status="OPERATIONAL", criticality_weight=0.95),
            "HOSPITAL_PRIMARY": SystemicNode("HOSPITAL_PRIMARY", "ProyectoSalud", capacity=150.0, current_load=110.0, backup_battery_hours=12.0, status="OPERATIONAL", criticality_weight=1.0),
            "INTERMODAL_PORT": SystemicNode("INTERMODAL_PORT", "ProyectoSeamlessIntermodalHub", capacity=300.0, current_load=220.0, backup_battery_hours=3.0, status="OPERATIONAL", criticality_weight=0.75),
            "URBAN_MOBILITY_H3": SystemicNode("URBAN_MOBILITY_H3", "AppViajes", capacity=200.0, current_load=140.0, backup_battery_hours=2.5, status="OPERATIONAL", criticality_weight=0.7),
            "OT_SCADA_MESH": SystemicNode("OT_SCADA_MESH", "ProyectoZeroTrustOTMesh", capacity=100.0, current_load=50.0, backup_battery_hours=8.0, status="OPERATIONAL", criticality_weight=0.95)
        }

    def _build_coupling_matrix(self) -> np.ndarray:
        A = np.zeros((self.n, self.n))
        idx = {name: i for i, name in enumerate(self.node_names)}
        
        # Conexiones de interdependencia física
        A[idx["GRID_SUBSTATION"], idx["VPP_AGGREGATOR"]] = 0.40
        A[idx["GRID_SUBSTATION"], idx["DESAL_PLANT"]] = 0.15
        A[idx["GRID_SUBSTATION"], idx["HOSPITAL_PRIMARY"]] = 0.20
        A[idx["GRID_SUBSTATION"], idx["PHARMA_DEPOT"]] = 0.15
        A[idx["GRID_SUBSTATION"], idx["INTERMODAL_PORT"]] = 0.10
        
        A[idx["VPP_AGGREGATOR"], idx["URBAN_MOBILITY_H3"]] = 0.50
        A[idx["VPP_AGGREGATOR"], idx["PHARMA_DEPOT"]] = 0.30
        
        A[idx["OT_SCADA_MESH"], idx["GRID_SUBSTATION"]] = 0.50
        A[idx["OT_SCADA_MESH"], idx["INTERMODAL_PORT"]] = 0.50
        
        return A

    def simulate_shock_cascade(
        self,
        target_node: str = "GRID_SUBSTATION",
        shock_magnitude: float = 1.0,
        scenario_name: str = "BLACKOUT_CYBER_PHYSICAL"
    ) -> Dict[str, Any]:
        """
        Ejecuta la propagación paso a paso de la falla en cascada hasta la estabilización.
        """
        print(f"⚡ [Cascading Failure Engine] Inyectando shock '{scenario_name}' en nodo '{target_node}'...")
        # Reset de topología
        self.nodes = self._initialize_topology()
        idx_map = {name: i for i, name in enumerate(self.node_names)}
        
        target_idx = idx_map[target_node]
        self.nodes[target_node].status = "FAILED"
        self.nodes[target_node].current_load = 0.0
        
        cascade_history = []
        q_history = []
        
        # Calidad de servicio inicial Q0
        total_weight = sum(n.criticality_weight for n in self.nodes.values())
        q0 = sum(n.criticality_weight for n in self.nodes.values() if n.status == "OPERATIONAL") / total_weight
        q_history.append(q0)
        
        step = 0
        max_steps = 24 # 24 horas de simulación de crisis
        new_failures = True
        
        while step < max_steps and new_failures:
            step += 1
            new_failures = False
            
            for i, name in enumerate(self.node_names):
                node = self.nodes[name]
                if node.status == "FAILED":
                    # Redistribuir su carga a nodos vecinos dependientes
                    for j, dest_name in enumerate(self.node_names):
                        dest_node = self.nodes[dest_name]
                        if dest_node.status in ["OPERATIONAL", "DEGRADED"]:
                            transfer = node.capacity * self.coupling_matrix[i, j]
                            dest_node.current_load += transfer * 0.4
                            
                            # Si excede capacidad y no tiene batería suficiente
                            if dest_node.current_load > dest_node.capacity:
                                if dest_node.backup_battery_hours < (step * 0.5):
                                    dest_node.status = "FAILED"
                                    new_failures = True
                                    print(f"  🔴 [Colapso Cascada] Paso {step}h: '{dest_name}' falló por sobrecarga ({dest_node.current_load:.1f}/{dest_node.capacity:.1f} MW)")
                                else:
                                    dest_node.status = "DEGRADED"
                                    dest_node.backup_battery_hours -= 0.5

            # Medir calidad de servicio sistémica Q(t)
            qt = sum(n.criticality_weight * (1.0 if n.status == "OPERATIONAL" else (0.5 if n.status == "DEGRADED" else 0.0))
                     for n in self.nodes.values()) / total_weight
            q_history.append(qt)

        # Fase de recuperación (Restauración de la red)
        mttr_hours = step * 1.5 + np.random.uniform(2.0, 5.0)
        
        # Índice de Resiliencia R (Área bajo la curva Q(t))
        r_index = float(np.mean(q_history))
        failed_count = sum(1 for n in self.nodes.values() if n.status == "FAILED")
        
        economic_loss = failed_count * 185000.0 * (mttr_hours / 4.0)
        unserved_energy = (failed_count * 120.0) * mttr_hours
        pharma_loss = 2500 if self.nodes["PHARMA_DEPOT"].status == "FAILED" else 0
        
        now = time.time()
        with sqlite3.connect(DB_PATH) as conn:
            c = conn.cursor()
            c.execute("""
                INSERT INTO cascading_resilience_telemetry
                (timestamp, shock_scenario, initial_failed_node, total_nodes, cascade_steps, 
                 nodes_failed_count, resilience_index_r, mttr_hours, economic_loss_usd, unserved_energy_mwh, pharma_spoilage_units)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                now, scenario_name, target_node, self.n, step, failed_count,
                r_index, mttr_hours, economic_loss, unserved_energy, pharma_loss
            ))
            conn.commit()

        print(f"  ✓ Índice de Resiliencia Sistémica R: {r_index:.3f} | MTTR: {mttr_hours:.1f}h")
        print(f"  ✓ Nodos colapsados: {failed_count}/{self.n} | Pérdida Económica: ${economic_loss:,.2f}")
        print("✅ [Cascading Failure Engine] Simulación de colapso y resiliencia completada.")
        
        return {
            "scenario": scenario_name,
            "initial_target": target_node,
            "resilience_index_r": round(r_index, 3),
            "mttr_hours": round(mttr_hours, 1),
            "nodes_failed": f"{failed_count}/{self.n}",
            "economic_loss_usd": round(economic_loss, 2),
            "status": "CASCADING_SIMULATION_COMPLETED"
        }

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Motor de Fallas en Cascada y Resiliencia Sistémica")
    parser.add_argument("--target", type=str, default="GRID_SUBSTATION", help="Nodo inicial a sabotear")
    parser.add_argument("--scenario", type=str, default="SUBSTATION_CYBER_ATTACK", help="Nombre del escenario")
    args = parser.parse_args()
    
    engine = CascadingFailureShockEngine()
    engine.simulate_shock_cascade(args.target, scenario_name=args.scenario)
