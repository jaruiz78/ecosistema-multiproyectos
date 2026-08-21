import re

with open("fourier_pinn_wall_diffusion.py", "r") as f:
    code = f.read()

# I will just ensure numpy is imported and replace the solve_transient_24h method
import_addition = "import numpy as np\nimport math\nfrom typing import Dict, List, Any, Tuple"
code = re.sub(r'import math\nfrom typing import Dict, List, Any, Tuple', import_addition, code)

new_solver_code = """
    def solve_transient_24h(self, t_ext_hourly: List[float], t_int_hourly: List[float],
                             solar_rad_hourly: List[float], dt_seconds: float = 300.0) -> Dict[str, Any]:
        \"\"\"
        Simula la propagación de onda de calor durante 24h a paso de tiempo dt_seconds usando NumPy.
        \"\"\"
        num_hours = len(t_ext_hourly)
        steps_per_hour = int(3600.0 / dt_seconds)
        total_steps = num_hours * steps_per_hour

        # Arrays numpy para constantes y estado
        n_nodes = len(self.mesh_nodes)
        node_temps = np.zeros(n_nodes)
        alphas = np.array([n["layer"].alpha for n in self.mesh_nodes])
        dxs = np.array([n["dx"] for n in self.mesh_nodes])
        r_factors = alphas * dt_seconds / (dxs * dxs)
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
"""

# Now replace the function in code
import re
code = re.sub(
    r'    def solve_transient_24h\(self, t_ext_hourly: List\[float\],.*?def get_default_roof_and_facade_profiles\(\) -> Dict\[str, Any\]:', 
    new_solver_code, 
    code, 
    flags=re.DOTALL
)

with open("fourier_pinn_wall_diffusion.py", "w") as f:
    f.write(code)

print("Patch applied successfully.")
