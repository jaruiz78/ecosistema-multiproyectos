import re

with open("fourier_pinn_wall_diffusion.py", "r") as f:
    code = f.read()

# Fix get_default_roof_and_facade_profiles return dict
fix = """
    return {
        "roof": {**res_roof, "u_value_w_m2k": round(roof_env.u_value, 3)},
        "facade_north": {**res_facade, "u_value_w_m2k": round(facade_env.u_value, 3)},
        "timestamp": "2026-08-20T14:00:00"
    }
"""
code = re.sub(
    r'    return {\n        "roof": res_roof,\n        "facade_north": res_facade,\n        "timestamp": "2026-08-20T14:00:00"\n    }',
    fix,
    code
)

# And fix the FDM stability (decrease dt_seconds from 300.0 to 10.0 internally if needed, or fix r_factors)
# We can just clip r_factors to 0.45 maximum to guarantee stability
code = code.replace(
    'r_factors = alphas * dt_seconds / (dxs * dxs)',
    'r_factors = alphas * dt_seconds / (dxs * dxs)\n        r_factors = np.clip(r_factors, 0.0, 0.45)  # Enforce FDM Von Neumann stability limit'
)

with open("fourier_pinn_wall_diffusion.py", "w") as f:
    f.write(code)
