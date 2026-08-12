import numpy as np
from hypothesis import given, strategies as st
from tensor_gnn_core import EnsembleKalmanFilter

@given(
    obs_val_1=st.floats(min_value=-1000, max_value=1000),
    obs_val_2=st.floats(min_value=-1000, max_value=1000)
)
def test_enkf_stability_and_covariance_non_negative(obs_val_1, obs_val_2):
    enkf = EnsembleKalmanFilter(n_ensembles=50, state_dim=2, obs_dim=2)
    F = np.eye(2)
    
    for _ in range(5):
        enkf.predict(F)
        obs = np.array([obs_val_1, obs_val_2])
        enkf.update(obs)
    
    cov = enkf.get_covariance_trace()
    assert cov >= 0, "La traza de la covarianza no puede ser negativa"
    
    state = enkf.get_mean_state()
    assert state.shape == (2,), "La forma del estado asimilado es incorrecta"
    assert not np.isnan(state).any(), "El estado asimilado contiene NaNs"
