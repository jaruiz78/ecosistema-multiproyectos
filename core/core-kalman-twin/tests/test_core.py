import unittest
import numpy as np
from src import EnsembleKalmanFilter

class Testcore_kalman_twin(unittest.TestCase):
    def test_enkf_convergence(self):
        enkf = EnsembleKalmanFilter(ensemble_size=100, state_dim=2, obs_dim=1)
        F = np.eye(2)
        Q = np.eye(2) * 0.01
        H = np.array([[1.0, 0.0]])
        R = np.array([[0.1]])
        
        enkf.predict(F, Q)
        enkf.update(np.array([5.0]), H, R)
        
        self.assertLess(enkf.get_covariance_trace(), 10.0)
        self.assertAlmostEqual(enkf.get_state_mean()[0], 5.0, places=0)

if __name__ == '__main__':
    unittest.main()
