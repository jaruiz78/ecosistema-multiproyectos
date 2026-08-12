# Arquitectura del "Contenedor de Schrödinger" (Unified Twin Core)

## Visión General
Esta arquitectura resuelve la paridad Dev/Prod conservando una **única imagen Docker** (`python:3.12-slim`) en Artifact Registry, mientras adapta su huella de memoria y latencia dinámicamente según el entorno (`APP_ENV=LOCAL|BETA|PROD`).

## Principios Fundamentales
1. **Lazy Loading de SDKs**: Los SDKs pesados (`google.cloud.*`, `pandas`, `sklearn`, `mesa`, `pypsa`) NO se importan en la cabecera del archivo. Se cargan de forma diferida en el scope local de cada endpoint o función cuando se requiera.
2. **Servidor ASGI Granian en Rust**: Sustitución de Uvicorn por Granian (`CMD ["granian", "--interface", "asgi", ...]` con 2 workers y 1 hilo) para minimizar el consumo de RAM en idle (~18MB).
3. **Cero-Copia Apache Arrow IPC**: Lectura directa de streams binarios IPC desde Go en estructuras Polars/NumPy sin pasar por sintaxis JSON ni duplicar la memoria en RAM.
4. **Garbage Collection Defensivo**: Invocación explícita de `gc.collect()` tras llamadas gRPC para liberar canalizaciones off-heap.
5. **Segregación Hot-Path / Cold-Path**:
   - **Hot-Path (Cloud Run Service)**: 2GB RAM, 2 CPU, `--concurrency=100`, `--cpu-boost`.
   - **Cold-Path (Cloud Run Jobs)**: 8GB RAM, 4 CPU para simulaciones masivas (Mesa/PyPSA).
