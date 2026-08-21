import numba
import numpy as np
import time

@numba.njit(fastmath=True)
def fourier_fdm_kernel_numba(node_temps, alphas, dxs, r_factors, k_vals, t_ext_arr, t_int_arr, sol_rad_arr, steps_per_hour, total_steps, num_hours, dt_seconds):
    n_nodes = len(node_temps)
    results_q_in = np.zeros(num_hours)
    results_t_int = np.zeros(num_hours)
    
    alpha_abs = 0.65
    h_ext = 25.0
    h_int = 7.7
    
    current_temps = np.copy(node_temps)
    new_temps = np.copy(node_temps)
    
    for step in range(total_steps):
        h_idx = min(num_hours - 1, step // steps_per_hour)
        t_ext = t_ext_arr[h_idx]
        t_int = t_int_arr[h_idx]
        sol_rad = sol_rad_arr[h_idx]
        
        t_sol_air = t_ext + (alpha_abs * sol_rad) / h_ext
        
        # Boundary nodes
        new_temps[0] = current_temps[0] + r_factors[0] * (t_sol_air - 2.0 * current_temps[0] + current_temps[1])
        new_temps[-1] = current_temps[-1] + r_factors[-1] * (current_temps[-2] - 2.0 * current_temps[-1] + t_int)
        
        # Inner nodes
        for i in range(1, n_nodes - 1):
            new_temps[i] = current_temps[i] + r_factors[i] * (current_temps[i-1] - 2.0 * current_temps[i] + current_temps[i+1])
            
        # Update
        for i in range(n_nodes):
            current_temps[i] = new_temps[i]
            
        if step % steps_per_hour == 0:
            q_in = h_int * (current_temps[-1] - t_int)
            results_q_in[h_idx] = q_in
            results_t_int[h_idx] = current_temps[-1]
            
    return results_q_in, results_t_int

# Benchmark
n_nodes = 30
node_temps = np.ones(n_nodes) * 25.0
alphas = np.ones(n_nodes) * 1e-6
dxs = np.ones(n_nodes) * 0.01
r_factors = np.ones(n_nodes) * 0.3
k_vals = np.ones(n_nodes) * 0.8
t_ext = np.linspace(20, 38, 24)
t_int = np.ones(24) * 24.0
sol = np.zeros(24)
sol[12] = 1000.0

# Warmup JIT
fourier_fdm_kernel_numba(node_temps, alphas, dxs, r_factors, k_vals, t_ext, t_int, sol, 12, 288, 24, 300.0)

# Timing
t0 = time.perf_counter()
for _ in range(1000):
    fourier_fdm_kernel_numba(node_temps, alphas, dxs, r_factors, k_vals, t_ext, t_int, sol, 12, 288, 24, 300.0)
t1 = time.perf_counter()
print(f"Numba Kernel: 1000 runs in {(t1 - t0)*1000:.2f} ms ({(t1 - t0):.5f} ms per run)")
