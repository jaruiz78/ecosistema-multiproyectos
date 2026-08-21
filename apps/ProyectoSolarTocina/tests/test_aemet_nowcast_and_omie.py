#!/usr/bin/env python3
"""
test_aemet_nowcast_and_omie.py
==============================
Tests unitarios para el servicio de nowcasting solar satelital (AEMET/Open-Meteo)
y el Filtro de Kalman multizona (EnKF).
"""

import unittest
from aemet_radar_satellite_service import SolarNowcastingService
from kalman_multizone_twin import KalmanMultizoneTwin


class TestNowcastAndKalman(unittest.TestCase):

    def test_solar_nowcasting_service_structure(self):
        """Verifica la respuesta del servicio de nowcasting satelital a 60 minutos."""
        service = SolarNowcastingService(lat=37.60, lon=-5.73)
        res = service.fetch_satellite_and_solar_nowcast()

        self.assertIn("timestamp", res)
        self.assertIn("solar_zenith_elevation_deg", res)
        self.assertIn("realtime_irradiance", res)
        self.assertIn("nowcast_60min", res)
        self.assertEqual(len(res["nowcast_60min"]), 4)

        for step in res["nowcast_60min"]:
            self.assertIn("offset_minutes", step)
            self.assertIn("projected_pv_kw", step)
            self.assertIn("solar_drop_risk", step)
            self.assertTrue(step["projected_pv_kw"] >= 0.0)

    def test_kalman_multizone_twin_prediction_and_update(self):
        """Verifica los pasos de predicción y actualización con covarianza."""
        twin = KalmanMultizoneTwin()
        self.assertEqual(len(twin.state), 6)

        # Paso de predicción
        prior_state = twin.predict_step(t_ext=27.0, q_hvac_salon=1.5)
        self.assertEqual(len(prior_state), 6)

        # Paso de asimilación
        obs = {
            "salon": 28.9,
            "despacho": 30.5,
            "cochera": 29.2,
            "patio": 27.0
        }
        res = twin.update_observation(obs)

        self.assertIn("state", res)
        self.assertIn("trace_covariance", res)
        self.assertIn("uncertainty_by_zone", res)
        self.assertEqual(len(res["state"]), 6)
        
        # Verificar que el offset del despacho (-1.5°C por peana monitor) se aplica
        self.assertTrue(res["state"]["despacho"] < 30.0)

    def test_kalman_multizone_twin_convergence(self):
        """Verifica que con múltiples asimilaciones la traza de covarianza disminuye."""
        twin = KalmanMultizoneTwin()
        obs = {"salon": 28.9, "despacho": 30.5, "cochera": 29.2, "patio": 27.0}
        
        initial_trace = sum(twin.p_cov[i][i] for i in range(6))
        
        for _ in range(5):
            twin.predict_step(t_ext=27.0, q_hvac_salon=1.0)
            res = twin.update_observation(obs)
            
        final_trace = res["trace_covariance"]
        self.assertTrue(final_trace < initial_trace)


if __name__ == "__main__":
    unittest.main()
