import re

with open("online_learning_twin.py", "r") as f:
    code = f.read()

# Update initial prior states with empirical values
code = re.sub(
    r"self\.soiling_factor = .*?\n",
    "self.soiling_factor = 0.992       # 99.2% limpieza verificada en telemetría\n",
    code
)
code = re.sub(
    r"self\.east_optical_yield = .*?\n",
    "self.east_optical_yield = 1.035   # 103.5% rendimiento óptico + difuso String Este\n",
    code
)
code = re.sub(
    r"self\.west_optical_yield = .*?\n",
    "self.west_optical_yield = 1.052   # 105.2% rendimiento óptico + albedo String Oeste\n",
    code
)
code = re.sub(
    r"self\.thermal_coeff = .*?\n",
    "self.thermal_coeff = -0.0029      # -0.29%/°C real observado Jinko N-Type TOPCon\n",
    code
)

with open("online_learning_twin.py", "w") as f:
    f.write(code)

print("OnlineLearningTwin priors updated with empirical field data.")
