"""
Test Suite: IoT Automation (Daikin, Smart Plugs, Environmental Sensors) & Naturgy Virtual Battery
Zero-Mockito / Hermetic Test Execution
"""

import unittest
import os
import json
from datetime import datetime

from daikin_iot_automation import DaikinIoTController
from smart_plugs_manager import SmartPlugsManager
from environmental_sensors_manager import EnvironmentalSensorsManager
from naturgy_virtual_battery_controller import NaturgyVirtualBatteryController

class TestIoTAutomationAndVirtualBattery(unittest.TestCase):
    def setUp(self):
        self.test_dir = os.path.join(os.path.dirname(__file__), "test_data")
        os.makedirs(self.test_dir, exist_ok=True)
        self.daikin_cfg = os.path.join(self.test_dir, "test_daikin_cfg.json")
        self.plugs_cfg = os.path.join(self.test_dir, "test_plugs_cfg.json")
        self.sensors_cfg = os.path.join(self.test_dir, "test_sensors_cfg.json")
        self.vb_cfg = os.path.join(self.test_dir, "test_vb_cfg.json")

    def tearDown(self):
        for f in [self.daikin_cfg, self.plugs_cfg, self.sensors_cfg, self.vb_cfg]:
            if os.path.exists(f):
                os.remove(f)

    # 1. TEST DAIKIN IOT CONTROLLER
    def test_daikin_seasonal_summer_precooling(self):
        ctrl = DaikinIoTController(config_path=self.daikin_cfg)
        # Verano, 14:00 h, Sol Excedente 2.5 kW, 36°C, SoC 85%
        res = ctrl.evaluate_seasonal_automation(
            current_hour=14,
            current_month=7,
            outdoor_temp_c=36.0,
            solar_surplus_kw=2.5,
            battery_soc=85.0
        )
        self.assertTrue(res["is_summer"])
        self.assertFalse(res["is_winter"])
        self.assertTrue(len(res["actions_taken"]) > 0)
        unit = ctrl.get_unit("daikin_salon")
        self.assertTrue(unit["status"]["power_on"])
        self.assertEqual(unit["status"]["target_temp_c"], 22.5)
        self.assertEqual(unit["status"]["mode"], "cool")

    def test_daikin_seasonal_winter_preheating(self):
        ctrl = DaikinIoTController(config_path=self.daikin_cfg)
        # Invierno, 13:00 h, Sol Excedente 2.0 kW, 14°C, SoC 60%
        res = ctrl.evaluate_seasonal_automation(
            current_hour=13,
            current_month=1,
            outdoor_temp_c=14.0,
            solar_surplus_kw=2.0,
            battery_soc=60.0
        )
        self.assertFalse(res["is_summer"])
        self.assertTrue(res["is_winter"])
        self.assertTrue(len(res["actions_taken"]) > 0)
        unit = ctrl.get_unit("daikin_salon")
        self.assertTrue(unit["status"]["power_on"])
        self.assertEqual(unit["status"]["target_temp_c"], 22.5)
        self.assertEqual(unit["status"]["mode"], "heat")
        self.assertEqual(unit["status"]["fan_direction"], "floor_60")

    # 2. TEST SMART PLUGS MANAGER
    def test_smart_plugs_ev_solar_dispatch(self):
        mgr = SmartPlugsManager(config_path=self.plugs_cfg)
        # Excedente alto 2.8 kW a las 14:00 h con batería al 90%
        res = mgr.evaluate_surplus_dispatch(solar_surplus_kw=2.8, battery_soc=90.0, current_hour=14)
        ev_plug = mgr.get_plug("omoda7_ev_schuko")
        self.assertTrue(ev_plug["state"]["power_on"])

        # Caída de sol a 0.3 kW -> Auto-OFF
        res_off = mgr.evaluate_surplus_dispatch(solar_surplus_kw=0.3, battery_soc=85.0, current_hour=14)
        self.assertFalse(ev_plug["state"]["power_on"])

    # 3. TEST ENVIRONMENTAL SENSORS MANAGER
    def test_environmental_sensors_dew_point_and_aggregation(self):
        mgr = EnvironmentalSensorsManager(config_path=self.sensors_cfg)
        # 26.0°C y 50% HR -> Punto de rocío ~14.8°C
        dp = mgr.calculate_dew_point(26.0, 50.0)
        self.assertAlmostEqual(dp, 14.8, delta=0.5)

        # Ingestar lectura Salón
        res = mgr.record_sensor_telemetry("sensor_salon", temp_c=25.5, humidity_pct=48.0, battery_pct=99)
        self.assertTrue(res["success"])
        self.assertEqual(res["readings"]["temperature_c"], 25.5)
        self.assertEqual(res["readings"]["comfort_index"], "Confort Óptimo")

    # 4. TEST NATURGY VIRTUAL BATTERY CONTROLLER
    def test_naturgy_virtual_battery_toggle_and_projection(self):
        vb = NaturgyVirtualBatteryController(config_path=self.vb_cfg)
        # Estado inicial Standby
        self.assertEqual(vb.config["status"], "STANDBY_CONTRACTED")

        # Conmutar a Activa
        toggle_res = vb.toggle_activation_status(is_active=True, activation_date="2026-09-01")
        self.assertTrue(toggle_res["is_active"])
        self.assertEqual(vb.config["status"], "ACTIVE_BILLING")

        # Proyección anual con potencia actual 4.60 kW
        proj = vb.generate_annual_projection()
        self.assertTrue(proj["is_active"])
        self.assertTrue(proj["zero_bill_months_count"] >= 3) # 3 meses a 0.00 € absoluto y 6 meses < 8 €
        self.assertTrue(proj["total_paid_year_eur"] < 350.0)
        self.assertTrue(proj["monthly_average_paid_eur"] < 30.0)

if __name__ == "__main__":
    unittest.main()
