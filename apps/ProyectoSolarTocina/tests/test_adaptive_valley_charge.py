"""
test_adaptive_valley_charge.py
Pruebas Unitarias Herméticas para el Optimizador Adaptativo de Carga Valle Fox-ESS P3 (00:00 a 06:30 h)
Ecosistema Solar Tocina - Los Rosales
"""

import unittest
from datetime import datetime
from valley_charge_scheduler import ValleyChargeScheduler

class TestAdaptiveValleyCharge(unittest.TestCase):
    def setUp(self):
        self.scheduler = ValleyChargeScheduler()

    def test_window_timing_bounds(self):
        """Verifica que la ventana comienza a las 00:00 h y termina a las 06:30 h"""
        cfg = self.scheduler.get_config()
        self.assertEqual(cfg["start_hour"], 0)
        self.assertEqual(cfg["end_hour"], 6)
        self.assertEqual(cfg.get("end_minute", 30), 30)

    def test_sunny_day_no_grid_charge(self):
        """Si mañana hace sol (>14 kWh), el sistema NO debe cargar de red"""
        # Evaluamos con día soleado simulado
        rec = self.scheduler.evaluate_dynamic_charge_needs(
            current_soc=70.0,
            forecast_solar_kwh=24.5,
            forecast_home_load_kwh=13.8,
            current_hour=1.0,
            current_home_w=350.0
        )
        self.assertFalse(rec["action_recommended"])
        self.assertEqual(rec["effective_target_soc_pct"], 70.0)
        self.assertEqual(rec["charge_power_w"], 0)

    def test_stormy_day_dynamic_power_modulation(self):
        """Si mañana es día de lluvia (4 kWh), calcula la potencia exacta para llegar antes de las 06:30 h"""
        rec = self.scheduler.evaluate_dynamic_charge_needs(
            current_soc=25.0,
            forecast_solar_kwh=4.2,
            forecast_home_load_kwh=14.0,
            current_hour=0.5, # 00:30 h
            current_home_w=400.0
        )
        self.assertTrue(rec["action_recommended"])
        self.assertGreater(rec["effective_target_soc_pct"], 80)
        self.assertGreater(rec["charge_power_w"], 800)
        self.assertLessEqual(rec["charge_power_w"], 2800)

    def test_icp_safety_throttle(self):
        """Si la casa consume 3.200 W en mitad de la noche, la carga se reduce para no pasar de 4.000 W"""
        safety = self.scheduler.evaluate_live_safety_override(
            home_load_w=3200.0,
            grid_import_w=3200.0
        )
        self.assertLessEqual(safety["recommended_charge_w"], 800.0)
        self.assertEqual(safety["safety_action"], "THROTTLE_DOWN")

if __name__ == "__main__":
    unittest.main()
