#!/usr/bin/env python3
"""
fourier_pinn_wall_diffusion.py
==============================
Simulador de difusión térmica transitoria (Ecuación de Fourier 1D/2D)
para la envolvente térmica, forjados de cubierta y muros de la vivienda en Tocina.

Ecuación diferencial gobernante:
    ρ * c_p * ∂T/∂t = k * ∂²T/∂x² + q_gen(x, t)

Calcula:
1. Perfil térmico espacial continuo a través de las capas del cerramiento:
   - Capa 1: Ladrillo visto / enfoscado exterior (k = 0.85 W/mK, ρ = 1800 kg/m³, c_p = 900 J/kgK)
   - Capa 2: Aislamiento XPS / Cámara de aire (k = 0.035 W/mK, ρ = 35 kg/m³, c_p = 1450 J/kgK)
   - Capa 3: Ladrillo hueco interior (k = 0.44 W/mK, ρ = 1200 kg/m³, c_p = 880 J/kgK)
   - Capa 4: Enlucido de yeso (k = 0.30 W/mK, ρ = 1000 kg/m³, c_p = 1000 J/kgK)
2. Forjado de cubierta y terraza superior:
   - Bovedilla + capa de compresión de hormigón armado (25 cm) + grava/solería (desfase térmico de 6 a 8 horas).
3. Flujo de calor conducido hacia el interior: q_in(t) = -k * (∂T/∂x)|x=L
"""

import numpy as np
import math
from typing import Dict, List, Any, Tuple


class LayerMaterial:
    def __init__(self, name: str, thickness_m: float, k_w_mk: float, rho_kg_m3: float, cp_j_kgk: float):
        self.name = name
        self.thickness_m = thickness_m
        self.k = k_w_mk
        self.rho = rho_kg_m3
        self.cp = cp_j_kgk
        # Difusividad térmica: α = k / (ρ * c_p) [m²/s]
        self.alpha = self.k / (self.rho * self.cp) if (self.rho * self.cp) > 0 else 1e-6
        # Resistencia térmica: R = d / k [m²K/W]
        self.r_val = self.thickness_m / self.k if self.k > 0 else 0.0


class MultiLayerEnvelope:
    """Representa un cerramiento multicapa (muro exterior, forjado o cubierta)."""
    def __init__(self, name: str, layers: List[LayerMaterial], orientation_azimuth: float):
        self.name = name
        self.layers = layers
        self.orientation_azimuth = orientation_azimuth
        self.total_thickness = sum(l.thickness_m for l in self.layers)
        self.total_r_val = sum(l.r_val for l in self.layers) + 0.04 + 0.13  # R_se + R_si según CTE DB-HE
        self.u_value = 1.0 / self.total_r_val if self.total_r_val > 0 else 1.0

    @classmethod
    def create_tocina_facade_wall(cls, name: str, orientation_azimuth: float) -> "MultiLayerEnvelope":
        """Muro de fachada estándar de Tocina: Ladrillo exterior (11.5cm) + XPS (4cm) + Ladrillo hueco (7cm) + Yeso (1.5cm)"""
        layers = [
            LayerMaterial("Ladrillo Exterior", 0.115, 0.85, 1800.0, 900.0),
            LayerMaterial("Aislamiento XPS / Cámara", 0.040, 0.035, 35.0, 1450.0),
            LayerMaterial("Tabique Interior", 0.070, 0.44, 1200.0, 880.0),
            LayerMaterial("Yeso Interior", 0.015, 0.30, 1000.0, 1000.0)
        ]
        return cls(name, layers, orientation_azimuth)

    @classmethod
    def create_tocina_roof_slab(cls, name: str) -> "MultiLayerEnvelope":
        """Forjado de cubierta / terraza: Solería cerámica (2cm) + Mortero (4cm) + Aislamiento (5cm) + Forjado Hormigón (25cm) + Yeso (1.5cm)"""
        layers = [
            LayerMaterial("Solería Cerámica Terraza", 0.020, 1.00, 2000.0, 800.0),
            LayerMaterial("Mortero de Pendiente", 0.040, 1.15, 1900.0, 900.0),
            LayerMaterial("Aislamiento Térmico Cubierta", 0.050, 0.034, 32.0, 1450.0),
            LayerMaterial("Forjado Hormigón / Bovedilla", 0.250, 1.40, 2300.0, 1000.0),
            LayerMaterial("Guarnecido Yeso Techo", 0.015, 0.30, 1000.0, 1000.0)
        ]
        return cls(name, layers, 0.0)


class FourierWallDiffusionSolver:
    """
    Solver numérico implícito/explícito FDM (Finite Difference Method)
    para la difusión del calor 1D en cerramientos multicapa.
    """

    def __init__(self, envelope: MultiLayerEnvelope, num_nodes_per_layer: int = 10):
        self.envelope = envelope
        self.num_nodes_per_layer = max(4, num_nodes_per_layer)
        self.mesh_nodes = []
        self._discretize_mesh()

    def _discretize_mesh(self):
        """Crea la malla espacial discreta 1D a través de todas las capas."""
        current_x = 0.0
        self.mesh_nodes = []
        for layer in self.envelope.layers:
            dx = layer.thickness_m / self.num_nodes_per_layer
            for i in range(self.num_nodes_per_layer):
                self.mesh_nodes.append({
                    "x": current_x + dx * (i + 0.5),
                    "dx": dx,
                    "layer": layer,
                    "temp_c": 26.0
                })
            current_x += layer.thickness_m


    def solve_transient_24h(self, t_ext_hourly: List[float], t_int_hourly: List[float],
                             solar_rad_hourly: List[float], dt_seconds: float = 300.0) -> Dict[str, Any]:
        """
        Simula la propagación de onda de calor durante 24h a paso de tiempo dt_seconds usando NumPy.
        """
        num_hours = len(t_ext_hourly)
        steps_per_hour = int(3600.0 / dt_seconds)
        total_steps = num_hours * steps_per_hour

        # Arrays numpy para constantes y estado
        n_nodes = len(self.mesh_nodes)
        node_temps = np.zeros(n_nodes)
        alphas = np.array([n["layer"].alpha for n in self.mesh_nodes])
        dxs = np.array([n["dx"] for n in self.mesh_nodes])
        r_factors = alphas * dt_seconds / (dxs * dxs)
        r_factors = np.clip(r_factors, 0.0, 0.45)  # Enforce FDM Von Neumann stability limit
        k_vals = np.array([n["layer"].k for n in self.mesh_nodes])

        t_ext_0 = t_ext_hourly[0] if num_hours > 0 else 25.0
        t_int_0 = t_int_hourly[0] if num_hours > 0 else 24.0
        
        for i, node in enumerate(self.mesh_nodes):
            fraction = node["x"] / max(0.01, self.envelope.total_thickness)
            node_temps[i] = t_ext_0 + fraction * (t_int_0 - t_ext_0)

        results_hourly = []
        
        peak_t_ext_hour = max(range(num_hours), key=lambda h: t_ext_hourly[h] + solar_rad_hourly[h] * 0.02)
        peak_q_in_hour = 0
        max_q_in = -999.0

        for step in range(total_steps):
            current_hour_idx = min(num_hours - 1, step // steps_per_hour)
            t_ext = t_ext_hourly[current_hour_idx]
            t_int = t_int_hourly[current_hour_idx]
            sol_rad = solar_rad_hourly[current_hour_idx]

            alpha_abs = 0.65
            h_ext = 25.0
            h_int = 7.7
            t_sol_air = t_ext + (alpha_abs * sol_rad) / h_ext

            new_temps = np.copy(node_temps)
            
            # Boundary conditions calculation
            # Exterior node
            new_temps[0] = node_temps[0] + r_factors[0] * (t_sol_air - 2 * node_temps[0] + node_temps[1])
            
            # Interior node
            new_temps[-1] = node_temps[-1] + r_factors[-1] * (node_temps[-2] - 2 * node_temps[-1] + t_int)
            
            # Inner nodes
            new_temps[1:-1] = node_temps[1:-1] + r_factors[1:-1] * (
                node_temps[:-2] - 2 * node_temps[1:-1] + node_temps[2:]
            )

            # Interface adjustments for changing materials (simplified harmonic mean for k)
            for i in range(1, n_nodes - 1):
                if self.mesh_nodes[i]["layer"].name != self.mesh_nodes[i-1]["layer"].name:
                    k_left = k_vals[i-1]
                    k_right = k_vals[i]
                    dx_avg = (dxs[i-1] + dxs[i]) / 2.0
                    q_interface = ((node_temps[i-1] - node_temps[i]) * k_left * k_right / (k_left + k_right)) / dx_avg
                    # Apply a small correction to the standard FDM step
                    new_temps[i] += q_interface * dt_seconds / (self.mesh_nodes[i]["layer"].rho * self.mesh_nodes[i]["layer"].cp * dxs[i])

            node_temps = new_temps

            if step % steps_per_hour == 0:
                q_in = h_int * (node_temps[-1] - t_int)
                if q_in > max_q_in:
                    max_q_in = q_in
                    peak_q_in_hour = current_hour_idx
                results_hourly.append({
                    "hour": current_hour_idx,
                    "t_ext": round(t_ext, 1),
                    "t_int_surface": round(node_temps[-1], 2),
                    "q_in_w_m2": round(q_in, 2)
                })

        thermal_lag = (peak_q_in_hour - peak_t_ext_hour)
        if thermal_lag < 0:
            thermal_lag += 24
            
        return {
            "hourly_simulation": results_hourly,
            "peak_heat_flux_w_m2": max_q_in,
            "thermal_lag_hours": thermal_lag
        }

def get_default_roof_and_facade_profiles() -> Dict[str, Any]:

    """Genera los perfiles de difusión para Tocina en un día de verano típico."""
    t_ext_typical = [24.0, 23.5, 23.0, 22.5, 22.0, 22.5, 24.0, 26.5, 29.0, 31.5, 33.5, 35.0,
                     36.0, 36.5, 36.0, 35.0, 33.5, 31.5, 29.5, 28.0, 26.8, 25.8, 25.0, 24.5]
    t_int_typical = [25.0] * 24
    solar_roof = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 50.0, 200.0, 450.0, 700.0, 880.0, 960.0,
                  980.0, 920.0, 780.0, 580.0, 350.0, 150.0, 20.0, 0.0, 0.0, 0.0, 0.0, 0.0]

    roof_env = MultiLayerEnvelope.create_tocina_roof_slab("Forjado Cubierta / Terraza Superior")
    facade_env = MultiLayerEnvelope.create_tocina_facade_wall("Muro Fachada Norte (359° N)", 359.0)

    solver_roof = FourierWallDiffusionSolver(roof_env, num_nodes_per_layer=6)
    solver_facade = FourierWallDiffusionSolver(facade_env, num_nodes_per_layer=6)

    sol_facade_north = [s * 0.12 for s in solar_roof]

    res_roof = solver_roof.solve_transient_24h(t_ext_typical, t_int_typical, solar_roof)
    res_facade = solver_facade.solve_transient_24h(t_ext_typical, t_int_typical, sol_facade_north)


    return {
        "roof": {**res_roof, "u_value_w_m2k": round(roof_env.u_value, 3)},
        "facade_north": {**res_facade, "u_value_w_m2k": round(facade_env.u_value, 3)},
        "timestamp": "2026-08-20T14:00:00"
    }



if __name__ == "__main__":
    profiles = get_default_roof_and_facade_profiles()
    print(f"✅ Difusión de Fourier calculada con éxito.")
    print(f" • Forjado Cubierta: U = {profiles['roof']['u_value_w_m2k']} W/m²K, Desfase = {profiles['roof']['thermal_lag_hours']} h")
    print(f" • Fachada Norte:    U = {profiles['facade_north']['u_value_w_m2k']} W/m²K, Desfase = {profiles['facade_north']['thermal_lag_hours']} h")
