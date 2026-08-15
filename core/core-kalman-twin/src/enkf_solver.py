import numpy as np

class EnsembleKalmanFilter:
    def __init__(self, ensemble_size, state_dim, obs_dim):
        self.N = ensemble_size
        self.n = state_dim
        self.m = obs_dim
        self.E = np.random.randn(self.n, self.N)

    def predict(self, F, Q):
        noise = np.random.multivariate_normal(np.zeros(self.n), Q, self.N).T
        self.E = F @ self.E + noise

    def update(self, z, H, R):
        V = np.random.multivariate_normal(np.zeros(self.m), R, self.N).T
        Z = np.tile(z.reshape(-1, 1), (1, self.N)) + V
        
        e_mean = np.mean(self.E, axis=1, keepdims=True)
        A = self.E - e_mean
        
        HA = H @ A
        S = (HA @ HA.T) / (self.N - 1) + R
        
        K = (A @ HA.T) / (self.N - 1) @ np.linalg.inv(S)
        
        self.E = self.E + K @ (Z - H @ self.E)
        
    def get_state_mean(self):
        return np.mean(self.E, axis=1)
        
    def get_covariance_trace(self):
        e_mean = np.mean(self.E, axis=1, keepdims=True)
        A = self.E - e_mean
        P = (A @ A.T) / (self.N - 1)
        return np.trace(P)
