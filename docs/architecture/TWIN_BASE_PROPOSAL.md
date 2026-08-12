# Propuesta Base: Gemelo Digital Unificado (Unified Twin)

## 1. Motivación: "Cero Simulaciones Aisladas"
Actualmente existen scripts de simulación dispersos como `hybrid_digital_twin_simulation.py` en repositorios locales (AppViajes, pctMultiMicroservices, SaaSRegantes). Esta fragmentación impide una orquestación centralizada.

El objetivo de esta fase (Fase 1) es extraer la lógica matemática, de modelado espacial y física de mercado, y consolidarla dentro del `corp-spring-boot-starter/unified_twin`.

## 2. Refactorización en `tensor_gnn_core.py`
Se ha creado un módulo base unificado (`tensor_gnn_core.py`) que absorbe la siguiente lógica extraída:

* **Haversine Vectorizado (CPU/GPU)**: Aprovechando NumPy y CuPy para compatibilidad con aceleradores de hardware y LiteRT.
* **Filtros de Kalman Ensemble (EnKF)**: Componente crucial para la reducción de jitter en telemetría de sensores y simulaciones espaciales continuas.

## 3. Manejo de Perturbaciones: Shocks Inyectables
En vez de hardcodear variables estáticas o dinámicas por cada simulación local, el motor unificado adopta una política de **Inyección de Shocks** (para físicas de mercado, clima, o rutas).

* **Regla Estricta de Covarianza:** Cada "shock" se inyecta como una secuencia. Se aplica una validación estricta utilizando el filtro EnKF. Si la covarianza del modelo tras la perturbación no logra converger a un valor inferior a `0.5` en un límite máximo de `10 Ticks`, el cambio es automáticamente rechazado (Rollback). Esto garantiza la estabilidad numérica del gemelo y evita divergencias incontroladas.

## 4. Interfaz de Red Ultra-rápida (Fase 2)
Se ha añadido una interfaz de red de alta velocidad (FastAPI) al núcleo. Este endpoint opera de manera asíncrona y está diseñado para soportar latencias mínimas:
* **`POST /api/v1/shocks/inject`**: Permite a los workers en Go y backends en Java 25 inyectar perturbaciones directamente, sometiéndolas a la validación estricta de EnKF.
* **`GET /api/v1/predictions/state`**: Expone el estado actualizado y las predicciones, permitiendo la consulta de datos en tiempo real.
El servidor de Python opera en el puerto `50051`, dejando preparado el terreno para implementaciones directas por gRPC si el throughput lo requiere más adelante.

## 5. Siguientes Pasos
- Migrar y adaptar el `H3SpatialHungarianOptimizer` hacia un modelo que conecte directamente con `tensor_gnn_core`.
- Iniciar las pruebas de estrés de los workers en Go y las rutinas Java con Virtual Threads consumiendo estos nuevos endpoints.
