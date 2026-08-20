# ADR-047: Optimización del Motor OSRM: Poda de Dataset, Despliegue en us-east1, minScale=0 y Caché O(1) de Corredores Frecuentes

## Estado
Aceptado e Implementado.

## Contexto y Problema
El microservicio de enrutamiento OSRM (`pct-osrm`) calcula distancias, geometrías y tiempos de viaje (ETA) para las reservas turísticas y de traslados en Panamá (PA) y República Dominicana (DO).

Anteriormente:
1. **Sobredimensionamiento del Grafo Vial**: El archivo compilado `merged.osrm` incluía cuadrantes de España (Sevilla), ocupando 103 MB de memoria y requiriendo contenedores de 2048 MiB RAM.
2. **Latencia de Red Transatlántica**: Estaba configurado en la región europea (`europe-west1`), lo que generaba un RTT de 120–135 ms hacia los clientes y servidores en Centroamérica y el Caribe.
3. **Peticiones HTTP Redundantes**: Alrededor del 80–85% de los transfers turísticos recorren rutas fijas (Aeropuerto Tocumen $\leftrightarrow$ Hoteles en PA; Aeropuertos Punta Cana/Las Américas $\leftrightarrow$ Zonas Turísticas en DO), realizando consultas HTTP idénticas a OSRM.

## Decisiones de Arquitectura

1. **Diferenciación de Dataset por Entorno**:
   - **Local y BETA**: Mantiene los cuadrantes de Panamá (PA), República Dominicana (DO) y Sevilla (España) para soporte de desarrollo local y testing.
   - **PRO (Producción)**: Poda estricta de la red vial limitándola exclusivamente a Panamá y República Dominicana:
     - Panamá: Bounding box `8.90, -79.65` a `9.15, -79.35`.
     - Dominicana: Bounding box `18.35, -70.15` a `18.75, -68.20`.
   - **Resultado**: Grafo OSRM reducido de 103 MB a **~20 MB (-80%)**.

2. **Dimensionamiento de Recursos y Serverless Scale-to-Zero (`minScale=0`)**:
   - **BETA (`cloudbuild_osrm_beta.yaml`)**: `min-instances=0` permanente para cero coste ocioso.
   - **PRO (`cloudbuild_osrm_prod.yaml`)**:
     - Memoria: **`512 MiB`** (frente a 2048 MiB anteriores).
     - vCPU: **`1.0 vCPU`**.
     - Escalado: **`min-instances=0`** (viable gracias al cold-start de carga en memoria de $< 350\text{ ms}$).

3. **Migración a Región `us-east1` en Producción**:
   - Despliegue en `us-east1` (Norteamérica Este), reduciendo el RTT a **~25-30 ms** hacia Panamá y República Dominicana.
   - Tiempo total de cálculo de ruta: **$< 35\text{ ms}$**.

4. **Caché en Memoria $O(1)$ de Corredores en `OsrmRoutingAdapter.java`**:
   - Implementación de `corridorCache` (`ConcurrentHashMap`) con clave normalizada a 4 decimales ($\approx 11\text{ m}$ de precisión), TTL de 24 horas y límite de capacidad protector de 10.000 entradas.
   - 80% de las consultas de ruta se resuelven en memoria en **$< 0.1\text{ ms}$** sin tráfico de red ni coste computacional.

## Consecuencias y Verificación
- **Coste de Infraestructura**: Reducido a prácticamente **`$0.01` a `$3.29 USD/mes`** (frente a $> \$1,200\text{ USD/mes}$ con Google Maps).
- **Rendimiento y Latencia**: Cold-start $< 350\text{ ms}$, RTT $< 30\text{ ms}$, y respuesta en caché $< 0.1\text{ ms}$.
- **Validación Automatizada**:
  - `OsrmRoutingAdapterTest` superado con validación de cache hit $O(1)$, cache miss, y circuit breaker.
  - `RoutingServiceTest` y suite completa de 309 tests de backend verificados en verde.
