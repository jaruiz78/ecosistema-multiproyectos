"""
test_pinn_and_multizone_thermal.py
Pruebas Unitarias Herméticas para el Modelo PINN Solar y Dinámica Multizona RC (Tocina)
"""

import unittest
from pinn_solar_model import PinnSolarModel
from daikin_iot_automation import DaikinIoTController

class TestPinnAndMultizoneThermal(unittest.TestCase):
    def setUp(self):
        self.pinn = PinnSolarModel()
        self.daikin = DaikinIoTController()

    def test_solar_geometry_azimuths_calibrated(self):
        """Verifica que los azimuts reales (89° Este / 269° Oeste) y coordenadas de Tocina estén activos"""
        self.assertEqual(self.pinn.east_azimuth, 89.0)
        self.assertEqual(self.pinn.west_azimuth, 269.0)
        self.assertAlmostEqual(self.pinn.lat, 37.59418, places=4)
        self.assertAlmostEqual(self.pinn.lon, -5.73972, places=4)

    def test_multizone_zones_definition(self):
        """Comprueba que las 6 zonas térmicas estén definidas con sus propiedades físicas"""
        self.assertIn("salon", self.pinn.zones)
        self.assertIn("despacho", self.pinn.zones)
        self.assertIn("cochera_baterias", self.pinn.zones)
        self.assertEqual(self.pinn.zones["despacho"]["q_int"], 0.30)
        self.assertEqual(self.pinn.zones["salon"]["area_m2"], 35.0)

    def test_precooling_triggered_by_hot_office(self):
        """Si el despacho está caliente (30.4 °C), el sistema debe recomendar refrigeración asistida por pasillo"""
        rec = self.pinn.compute_thermal_precooling_recommendation(
            outdoor_temp_c=27.0,
            current_hour=12,
            solar_surplus_kw=1.0,
            office_temp_c=30.4
        )
        self.assertTrue(rec["recommend_precooling"])
        self.assertEqual(rec["optimal_setpoint_c"], 22.5)
        self.assertTrue(rec.get("office_assisted_cooling", False))
        self.assertIn("despacho", rec["reason"].lower())

    def test_multizone_rc_thermal_simulation(self):
        """Simula la disipación del calor en el despacho cuando el split del salón está encendido"""
        initial_temps = {
            "salon": 24.0,
            "despacho": 30.4,
            "dormitorio": 25.0,
            "estudio_mujer": 25.0,
            "cochera_baterias": 29.0,
            "patio_exterior": 27.0
        }
        next_temps = self.pinn.simulate_multizone_temperatures(
            current_temps=initial_temps,
            outdoor_temp_c=27.0,
            daikin_salon_on=True,
            daikin_setpoint=22.5,
            dt_hours=1.0
        )
        self.assertLess(next_temps["despacho"], 30.4)
        self.assertGreater(next_temps["despacho"], 20.0)

    def test_daikin_automation_office_hot_trigger(self):
        """Verifica que el evaluador estacional active el split del salón si el despacho tiene alerta térmica"""
        res = self.daikin.evaluate_seasonal_automation(
            current_hour=12,
            current_month=8,
            outdoor_temp_c=28.0,
            solar_surplus_kw=0.8,
            battery_soc=80,
            indoor_office_temp_c=30.4
        )
        self.assertEqual(len(res["actions_taken"]), 1)
        self.assertIn("Despacho", res["actions_taken"][0])

if __name__ == "__main__":
    unittest.main()
