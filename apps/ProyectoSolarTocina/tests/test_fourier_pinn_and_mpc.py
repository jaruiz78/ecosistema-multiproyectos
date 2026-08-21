#!/usr/bin/env python3
"""
test_fourier_pinn_and_mpc.py
============================
Tests unitarios para el solver de difusión térmica de Fourier 1D/2D
y el optimizador MPC en horizonte rodante de 48 horas.
"""

import unittest
from fourier_pinn_wall_diffusion import (
    LayerMaterial, MultiLayerEnvelope, FourierWallDiffusionSolver, get_default_roof_and_facade_profiles
)
from mpc_rolling_horizon_optimizer import MPCRollingHorizonOptimizer, get_sample_48h_mpc_plan


class TestFourierPINNAndMPC(unittest.TestCase):

    def test_layer_material_properties(self):
        """Verifica el cálculo de difusividad y resistencia térmica de capas."""
        mat = LayerMaterial("XPS", 0.04, 0.035, 35.0, 1450.0)
        self.assertAlmostEqual(mat.r_val, 0.04 / 0.035, places=3)
        self.assertTrue(mat.alpha > 0.0)

    def test_multilayer_envelope_u_value(self):
        """Verifica que el U-value de muros y cubiertas cumpla rangos físicos razonables."""
        wall = MultiLayerEnvelope.create_tocina_facade_wall("Fachada Norte", 359.0)
        roof = MultiLayerEnvelope.create_tocina_roof_slab("Cubierta")
        
        # U-value típico de fachadas aisladas: entre 0.3 y 0.9 W/m²K
        self.assertTrue(0.3 <= wall.u_value <= 0.9)
        self.assertTrue(0.3 <= roof.u_value <= 0.9)
        self.assertTrue(wall.total_thickness > 0.20)
        self.assertTrue(roof.total_thickness > 0.30)

    def test_fourier_solver_simulation_stability(self):
        """Verifica la estabilidad numérica y cálculo de desfase térmico en 24h."""
        envelope = MultiLayerEnvelope.create_tocina_roof_slab("Cubierta Test")
        solver = FourierWallDiffusionSolver(envelope, num_nodes_per_layer=5)

        t_ext = [25.0] * 24
        t_int = [24.0] * 24
        sol = [0.0] * 24
        sol[12] = 1000.0  # Pulso solar al mediodía

        res = solver.solve_transient_24h(t_ext, t_int, sol)
        self.assertEqual(len(res["hourly_simulation"]), 24)
        self.assertIn("thermal_lag_hours", res)
        self.assertTrue(res["thermal_lag_hours"] >= 0.0)
        self.assertTrue(res["peak_heat_flux_w_m2"] >= 0.0)

    def test_default_roof_and_facade_profiles(self):
        """Verifica la función factoría de perfiles por defecto para Tocina."""
        profiles = get_default_roof_and_facade_profiles()
        self.assertIn("roof", profiles)
        self.assertIn("facade_north", profiles)
        self.assertTrue(profiles["roof"]["u_value_w_m2k"] > 0)
        self.assertTrue(profiles["facade_north"]["u_value_w_m2k"] > 0)

    def test_mpc_rolling_horizon_optimization(self):
        """Verifica la resolución del optimizador MPC 48h con batería y EV."""
        optimizer = MPCRollingHorizonOptimizer(battery_cap_kwh=10.0, max_charge_kw=3.0, max_discharge_kw=3.0)
        
        pv_48h = [3.5 if 10 <= (i % 24) <= 17 else 0.0 for i in range(48)]
        load_48h = [0.6] * 48
        buy_48h = [0.15] * 48
        sell_48h = [0.06] * 48

        res = optimizer.solve_48h_schedule(pv_48h, load_48h, buy_48h, sell_48h, initial_soc=0.50, ev_target_kwh=10.0)

        self.assertEqual(res["horizon_hours"], 48)
        self.assertEqual(len(res["schedule"]), 48)
        self.assertTrue(res["self_sufficiency_pct"] >= 80.0)
        self.assertTrue(res["ev_energy_delivered_kwh"] > 0.0)
        self.assertTrue(res["total_cost_optimized_eur"] <= res["total_cost_unoptimized_eur"])

    def test_sample_48h_mpc_plan(self):
        """Verifica la función auxiliar del plan MPC por defecto."""
        plan = get_sample_48h_mpc_plan()
        self.assertIn("self_sufficiency_pct", plan)
        self.assertIn("virtual_battery_credit_earned_eur", plan)
        self.assertTrue(plan["total_pv_generation_kwh"] > 20.0)


if __name__ == "__main__":
    unittest.main()
