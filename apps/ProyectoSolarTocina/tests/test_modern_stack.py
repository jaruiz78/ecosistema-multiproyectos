#!/usr/bin/env python3
"""
test_modern_stack.py
====================
Tests unitarios para validar la pila tecnológica modernizada:
- DuckDB & Parquet exportación / lectura.
- Numba JIT aceleración termodinámica.
- Validación Pydantic v2.
"""

import os
import unittest

from pydantic import BaseModel, ValidationError

from duckdb_analytics_engine import PARQUET_EXPORT_PATH, duckdb_engine
from fourier_pinn_wall_diffusion import (
    get_default_roof_and_facade_profiles,
)


class TelemetryPayloadTest(BaseModel):
    solar_kw: float
    battery_soc: float


class TestModernStack(unittest.TestCase):

    def test_parquet_export_and_compression(self):
        """Verifica que DuckDB pueda exportar la BD a Parquet y que el fichero exista."""
        res = duckdb_engine.export_to_parquet()
        self.assertTrue(res.get("success", False))
        self.assertTrue(os.path.exists(PARQUET_EXPORT_PATH))
        self.assertTrue(os.path.getsize(PARQUET_EXPORT_PATH) > 0)

    def test_duckdb_climate_matrix_query(self):
        """Verifica que DuckDB consulte la matriz climática 5 años correctamente."""
        matrix = duckdb_engine.query_5yr_climate_matrix()
        self.assertIsInstance(matrix, list)
        if matrix:
            self.assertIn("month", matrix[0])
            self.assertIn("avg_solar_kw", matrix[0])

    def test_numba_fourier_solver_accuracy(self):
        """Verifica que el solver Numba produzca resultados consistentes con la física."""
        profiles = get_default_roof_and_facade_profiles()
        self.assertIn("roof", profiles)
        self.assertIn("facade_north", profiles)
        self.assertTrue(profiles["roof"]["u_value_w_m2k"] > 0)
        self.assertTrue(profiles["roof"]["thermal_lag_hours"] >= 0)
        self.assertTrue(profiles["roof"]["peak_heat_flux_w_m2"] > 0)

    def test_pydantic_schema_validation(self):
        """Verifica la validación estricta y rápida con Pydantic v2."""
        valid_obj = TelemetryPayloadTest(solar_kw=4.5, battery_soc=95.0)
        self.assertEqual(valid_obj.solar_kw, 4.5)
        self.assertEqual(valid_obj.battery_soc, 95.0)

        with self.assertRaises(ValidationError):
            TelemetryPayloadTest(solar_kw="invalid_number", battery_soc=95.0)


if __name__ == "__main__":
    unittest.main()
