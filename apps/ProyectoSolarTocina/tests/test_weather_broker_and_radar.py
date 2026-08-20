"""
Unit tests for Weather Broker, Radar Assimilation, and Weather Station Calculations.
Six Sigma Quality & Zero-Mockito Pure Domain Testing.
"""

import unittest
import os
import sys

# Ensure parent directory is in sys.path
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from weather_broker import (
    get_wind_direction_cardinal,
    WMO_WEATHER_CODES,
    get_radar_layers,
    get_current_weather_summary,
    get_solar_nowcast_minutely
)

class TestWeatherBrokerAndRadar(unittest.TestCase):

    def test_get_wind_direction_cardinal(self):
        """Verifica la conversión precisa de grados a los 16 rumbos cardinales."""
        self.assertEqual(get_wind_direction_cardinal(0), 'N')
        self.assertEqual(get_wind_direction_cardinal(360), 'N')
        self.assertEqual(get_wind_direction_cardinal(90), 'E')
        self.assertEqual(get_wind_direction_cardinal(180), 'S')
        self.assertEqual(get_wind_direction_cardinal(270), 'O')
        self.assertEqual(get_wind_direction_cardinal(45), 'NE')
        self.assertEqual(get_wind_direction_cardinal(135), 'SE')
        self.assertEqual(get_wind_direction_cardinal(225), 'SO')
        self.assertEqual(get_wind_direction_cardinal(315), 'NO')

    def test_wmo_weather_codes_coverage(self):
        """Verifica que los códigos estándar WMO estén mapeados correctamente con icono y descripción."""
        self.assertIn(0, WMO_WEATHER_CODES)
        self.assertEqual(WMO_WEATHER_CODES[0]['desc'], 'Cielo Despejado')
        self.assertEqual(WMO_WEATHER_CODES[0]['icon'], '☀️')
        
        self.assertIn(61, WMO_WEATHER_CODES)
        self.assertEqual(WMO_WEATHER_CODES[61]['desc'], 'Lluvia Ligera')
        self.assertEqual(WMO_WEATHER_CODES[61]['icon'], '🌧️')

    def test_current_weather_summary_structure(self):
        """Verifica que el resumen meteorológico en vivo contenga todos los KPIs esenciales."""
        summary = get_current_weather_summary()
        self.assertIsInstance(summary, dict)
        self.assertIn('temperature_c', summary)
        self.assertIn('apparent_temperature_c', summary)
        self.assertIn('humidity_percent', summary)
        self.assertIn('dew_point_c', summary)
        self.assertIn('pressure_hpa', summary)
        self.assertIn('wind_speed_kmh', summary)
        self.assertIn('wind_cardinal', summary)
        self.assertIn('wind_gusts_kmh', summary)
        self.assertIn('uv_index', summary)
        self.assertIn('cloud_cover_percent', summary)
        self.assertIn('cloud_layers', summary)
        self.assertIn('sun', summary)
        self.assertIn('today_stats', summary)
        
        # Validar rangos físicos
        self.assertGreaterEqual(summary['humidity_percent'], 0)
        self.assertLessEqual(summary['humidity_percent'], 100)
        self.assertGreaterEqual(summary['pressure_hpa'], 900)
        self.assertLessEqual(summary['pressure_hpa'], 1100)

    def test_radar_layers_structure(self):
        """Verifica que la consulta de capas de radar devuelva frames válidos y host seguro."""
        radar = get_radar_layers()
        self.assertIsInstance(radar, dict)
        self.assertIn('host', radar)
        self.assertIn('radar_frames', radar)
        self.assertIn('satellite_frames', radar)
        self.assertIn('center', radar)
        self.assertAlmostEqual(radar['center'][0], 37.5942, places=3)
        self.assertAlmostEqual(radar['center'][1], -5.7397, places=3)

    def test_solar_nowcast_minutely(self):
        """Verifica la serie temporal de 15 minutos de nowcasting solar."""
        nowcast = get_solar_nowcast_minutely()
        self.assertIsInstance(nowcast, dict)
        self.assertIn('timeline', nowcast)
        self.assertIn('sample_count', nowcast)
        self.assertEqual(nowcast['sample_count'], len(nowcast['timeline']))
        if nowcast['sample_count'] > 0:
            first_slot = nowcast['timeline'][0]
            self.assertIn('time', first_slot)
            self.assertIn('estimated_solar_kw', first_slot)
            self.assertIn('cloud_cover', first_slot)
            self.assertIn('dni_w_m2', first_slot)
            self.assertIn('dhi_w_m2', first_slot)
            self.assertIn('ghi_w_m2', first_slot)

if __name__ == '__main__':
    unittest.main()
