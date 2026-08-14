# ADR 003: Gemelo Digital Unificado (PEPS + EnKF) frente a Simulaciones Aisladas

## Estado
Aprobado (Consilium Romano)

## Contexto
La proliferación de scripts aislados de simulación física, económica y de tráfico generaba silos de información y divergencia matemática entre subsistemas (agua, energía, movilidad).

## Decisión
1. Prohibir la creación de simuladores independientes desconectados (*Política Cero Simulaciones Aisladas*).
2. Centralizar todas las interacciones físicas y económicas en el motor maestro [`tensor_gnn_core.py`](file:///home/jaruiz/Desarrollo/core/core-kalman-twin) mediante **Redes Tensoriales (PEPS)** y asimilación estocástica con **Filtro de Kalman de Ensamble (EnKF)**.
3. Exigir la convergencia de covarianza por debajo de 0.5 en 10 ticks en `simulations_telemetry.db` antes de certificar cualquier heurística.

## Consecuencias
* **Positivas:** Acoplamiento bidireccional real entre dominios (ej. demanda de vehículos eléctricos acoplada a la generación solar de microredes).
* **Negativas:** Mayor complejidad en la formulación tensorial de variables de entrada.
