import unittest
import importlib.util
from pathlib import Path
import numpy as np

# Carga hermética directa sin depender de sys.path
_solver_path = Path(__file__).resolve().parent.parent / "src" / "enkf_solver.py"
_spec = importlib.util.spec_from_file_location("enkf_solver", _solver_path)
_enkf_module = importlib.util.module_from_spec(_spec)
_spec.loader.exec_module(_enkf_module)
EnsembleKalmanFilter = _enkf_module.EnsembleKalmanFilter

class Testcore_kalman_twin(unittest.TestCase):
    def test_enkf_convergence(self):
        np.random.seed(42)
        enkf = EnsembleKalmanFilter(ensemble_size=200, state_dim=2, obs_dim=2)
        F = np.eye(2)
        Q = np.eye(2) * 0.001
        H = np.eye(2)
        R = np.eye(2) * 0.05

        # 5 ticks de asimilación estocástica
        for _ in range(5):
            enkf.predict(F, Q)
            enkf.update(np.array([5.0, 3.0]), H, R)

        self.assertLess(enkf.get_covariance_trace(), 0.1)
        self.assertAlmostEqual(float(enkf.get_state_mean()[0]), 5.0, places=1)
        self.assertAlmostEqual(float(enkf.get_state_mean()[1]), 3.0, places=1)

if __name__ == '__main__':
    unittest.main()
