"""
Arquitectura y especificación formal para test_core.py.

Documentación de Referencia:
- ADR: file:///home/jaruiz/Desarrollo/docs/adr/adr-003-unified-twin-peps-enkf.md
- Módulo Formativo: file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_3_unified_twin_math/03_asimilacion_de_datos_enkf.md
- Referencia Académica: Evensen (2003) Sequential Data Assimilation with EnKF (JGR)
"""
import unittest
import os
import sys
import numpy as np

sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '../src')))
from core_kalman_twin.enkf_solver import EnKFSolver

class TestCoreKalmanTwin(unittest.TestCase):
    def test_enkf_covariance_convergence(self):
        solver = EnKFSolver(n_states=5, process_noise=1e-4, measurement_noise=1e-2)
        initial_var = solver.get_variance()
        self.assertAlmostEqual(initial_var, 1.0, places=4)
        
        # Simular 20 mediciones constantes
        measurement = np.array([2.0, 2.0, 2.0, 2.0, 2.0])
        for _ in range(20):
            updated_state = solver.update(measurement)
            
        final_var = solver.get_variance()
        self.assertLess(final_var, 0.05, f"La varianza final debe converger por debajo de 0.05: {final_var}")
        np.testing.assert_allclose(updated_state, measurement, atol=0.1)

    def test_enkf_symmetry_and_positive_definiteness(self):
        solver = EnKFSolver(n_states=4, process_noise=1e-3, measurement_noise=1e-2)
        solver.update(np.array([1.0, -1.0, 0.5, -0.5]))
        # Verificar simetría exacta
        np.testing.assert_allclose(solver.P, solver.P.T, atol=1e-12)
        # Verificar que todos los autovalores sean estrictamente positivos
        eigenvalues = np.linalg.eigvalsh(solver.P)
        self.assertTrue(np.all(eigenvalues > 0.0), f"Autovalores no positivos: {eigenvalues}")

if __name__ == '__main__':
    unittest.main()
