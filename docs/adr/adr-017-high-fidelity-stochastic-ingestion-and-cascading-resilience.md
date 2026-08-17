# ADR 017: Ingesta Estocástica de Alta Fidelidad, MLOps Adaptativo Online y Resiliencia en Cascada

## Estado
Aprobado (Consilium Romano)

## Contexto
Para alcanzar un 100% de realismo empírico en el Gemelo Digital Unificado, era necesario superar las simplificaciones habituales de ruido gaussiano blanco, modelos estáticos y perturbaciones aisladas por componente, incorporando dinámica estocástica no lineal, deriva temporal de sensores (*sensor drift*), pérdidas de paquetes en redes móviles, recalibración de pesos en tiempo real y propagación de fallas en cascada a través de infraestructuras críticas interconectadas.

## Decisión
1. **Ingesta Estocástica No-Gaussiana (`high_fidelity_stochastic_telemetry_generator.py`):**
   - Adoptar procesos de difusión con saltos de Ornstein-Uhlenbeck:
     \[
     dX_t = \theta (\mu - X_t) dt + \sigma dW_t + J_t dN_t
     \]
     con saltos de cola pesada (Student-t, \(gl=3\) / Cauchy).
   - Modelar canales de red mediante cadenas de Markov de 4 estados (Line-of-Sight, Cañón Urbano, Cobertura Débil, Blackout/Túnel).
2. **MLOps Adaptativo y Calibración Continua Online (`online_continual_learning_enkf_pipeline.py`):**
   - Integrar detección de *Concept Drift* basada en ADWIN (Adaptive Windowing) con límites de Hoeffding.
   - Reajustar recursivamente los pesos de los modelos neuronales vía Filtro de Kalman por Conjuntos (EnKF) y cuantizar simétricamente en INT8 para ejecución edge en LiteRT.
   - Exigir validación cruzada purgada con embargo (*Purged Group TimeSplit* de Marcos López de Prado) en `purged_time_series_validation.py`.
3. **Simulador de Fallas en Cascada y Juegos de Nash-Stackelberg (`cascading_failure_shock_engine.py`, `nash_stackelberg_market_game.py`):**
   - Modelar la topología dirigida de dependencias entre redes eléctricas (VPP/Energía), cadena de frío (Pharma/Fleet), hospitales (Salud) y movilidad urbana (AppViajes), evaluando el Índice de Resiliencia Sistémica \(R\) y el MTTR.
   - Calcular equilibrios no cooperativos de Stackelberg y el Precio de la Anarquía (PoA).
4. **Persistencia Telemétrica Unificada:**
   - Registrar métricas de alta resolución en `data/simulations_telemetry.db` (tablas `high_fidelity_sensor_telemetry`, `online_calibration_telemetry`, `purged_cv_benchmarks`, `cascading_resilience_telemetry`, `nash_market_game_telemetry`).

## Consecuencias
* **Positivas:**
  - Realismo empírico del 100% en condiciones de campo extremas y crisis sistémicas.
  - Cero fugas de información (*data leakage*) en series temporales financieras y energéticas.
  - Modelos LiteRT INT8 auto-adaptativos ante cambios estructurales de mercado o clima.
* **Negativas:**
  - Incremento en la densidad de cómputo en la generación sintética, mitigado mediante vectorización NumPy en \(\mathcal{O}(1)/\mathcal{O}(N)\).
