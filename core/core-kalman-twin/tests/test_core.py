import sys
import os
import numpy as np
import pytest
sys.path.insert(0, os.path.join(os.path.dirname(__file__), "..", "src"))
from enkf_solver import EnsembleKalmanFilter

def test_enkf_convergence():
    # Sistema dinámico 2D
    N = 50
    n = 2
    m = 2
    enkf = EnsembleKalmanFilter(ensemble_size=N, state_dim=n, obs_dim=m, seed=42)
    
    F = np.eye(n)
    Q = np.eye(n) * 0.01
    H = np.eye(m, n)
    R = np.eye(m) * 0.05
    
    # 10 pasos de asimilación
    true_state = np.array([5.0, 3.0])
    for _ in range(10):
        enkf.predict(F, Q)
        z = true_state + np.random.normal(0, 0.05, m)
        enkf.update(z, H, R)
        
    mean = enkf.get_state_mean()
    cov_trace = enkf.get_covariance_trace()
    
    assert cov_trace < 0.10
    assert np.allclose(mean, true_state, atol=0.5)

def test_enkf_nonlinear_prediction():
    # Prueba con propagación no lineal: f(x) = x + dt * cos(x)
    N = 40
    n = 1
    m = 1
    enkf = EnsembleKalmanFilter(ensemble_size=N, state_dim=n, obs_dim=m, seed=123)
    
    def non_linear_dynamic(E):
        return E + 0.1 * np.cos(E)
        
    Q = np.array([[0.01]])
    H = np.array([[1.0]])
    R = np.array([[0.02]])
    
    for _ in range(5):
        enkf.predict_nonlinear(non_linear_dynamic, Q)
        z = np.array([1.5])
        enkf.update(z, H, R)
        
    assert enkf.get_covariance_trace() < 0.15

def test_enkf_cholesky_near_singular_resilience():
    # Prueba con matriz de observación casi singular y ruido bajo
    N = 30
    n = 2
    m = 2
    enkf = EnsembleKalmanFilter(ensemble_size=N, state_dim=n, obs_dim=m, seed=99)
    
    F = np.eye(2)
    Q = np.eye(2) * 1e-4
    H = np.array([[1.0, 1.0], [1.0, 1.00001]]) # Cuasi-singular
    R = np.eye(2) * 1e-5
    
    enkf.predict(F, Q)
    enkf.update(np.array([2.0, 2.0]), H, R)
    
    # La traza debe ser finita y positiva
    trace = enkf.get_covariance_trace()
    assert np.isfinite(trace)
    assert trace > 0.0

def test_enkf_high_dimensional_system():
    # Sistema 5D de estado con 3 observaciones
    N = 80
    n = 5
    m = 3
    enkf = EnsembleKalmanFilter(ensemble_size=N, state_dim=n, obs_dim=m, seed=777)
    
    F = np.eye(n) * 0.98
    Q = np.eye(n) * 0.005
    H = np.zeros((m, n))
    H[0, 0] = 1.0
    H[1, 2] = 1.0
    H[2, 4] = 1.0
    R = np.eye(m) * 0.02
    
    for _ in range(15):
        enkf.predict(F, Q)
        enkf.update(np.array([1.0, 2.0, 3.0]), H, R)
        
    mean = enkf.get_state_mean()
    assert mean.shape == (5,)
    assert enkf.get_covariance_trace() < 1.5

def test_enkf_invalid_invariants():
    with pytest.raises(ValueError):
        EnsembleKalmanFilter(ensemble_size=1, state_dim=2, obs_dim=2)
    with pytest.raises(ValueError):
        EnsembleKalmanFilter(ensemble_size=10, state_dim=0, obs_dim=2)

