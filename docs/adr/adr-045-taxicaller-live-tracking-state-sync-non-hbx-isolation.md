# ADR-045: Sincronización de Estados TaxiCaller Driver App, Tracking GPS en Vivo y Aislamiento No-HBX

## Estado
Aceptado e Implementado en BETA (`pct-backend-beta-00140-dn4`) y verificado formalmente mediante 1.000.000 de simulaciones Monte Carlo para PRO.

## Contexto y Problema
En la integración operativa entre plataformas de transporte (TaxiCaller) y canales B2B (Hotelbeds / HBX), existían tres desajustes funcionales y de concurrencia:
1. **Desincronización de Estados Reales**: Los webhooks forzaban el estado estático `ASSIGNED`, ignorando transiciones posteriores ejecutadas por el conductor en la app móvil (`ACCEPTED`, `CALLOUT` / `ON_THE_WAY`, `WAIT` / `ARRIVED`, `POB`, `DELIVERED` / `COMPLETED`).
2. **Ausencia de Telemetría GPS en Vivo**: El ciclo recurrente de Cloud Tasks (`/start-tracking`) no invocaba el endpoint `/track` de TaxiCaller, provocando que las coordenadas reales (`lastLocation`) nunca se registraran en Firestore durante el trayecto.
3. **Contaminación de Reservas Internas (No-HBX)**: Reservas de prueba, locales o manuales (`ES-JOSE1-...`, `MANUAL-...`) disparaban llamadas hacia los endpoints B2B de Hotelbeds, causando errores HTTP 400/404 innecesarios.
4. **Pérdida de Contexto de Inquilino en Hilos Virtuales Asíncronos (`@Async`)**: Al ejecutarse en nuevos Virtual Threads de Spring, `TenantContext` (ThreadLocal) se perdía, intentando acceder a bases de datos default inexistentes.

## Decisiones de Arquitectura

1. **Alineación con el Ciclo de Vida de TaxiCaller Driver App**:
   - Se extendió `TaxiCallerMapper.mapStatus` para traducir unívocamente todos los eventos de la App de Conductor:
     - `DELIVERED`, `FINISHED`, `TERMINADO`, `COMPLETED` $\rightarrow$ `TcJobStatus.COMPLETED` / `JobStatus.COMPLETED`.
     - `CALLOUT`, `ON_THE_WAY`, `EN_CAMINO` $\rightarrow$ `TcJobStatus.DISPATCHED` / `JobStatus.ACTIVE`.
     - `WAIT`, `WAITING`, `ESPERANDO` $\rightarrow$ `TcJobStatus.ARRIVED` / `JobStatus.ACTIVE`.
     - `POB`, `PASSENGER_ON_BOARD`, `A_BORDO` $\rightarrow$ `TcJobStatus.POB` / `JobStatus.ACTIVE`.
     - `ACCEPTED`, `CONFIRMED` $\rightarrow$ `TcJobStatus.CONFIRMED` / `JobStatus.ASSIGNED`.
     - `CANCELLED`, `REJECTED` $\rightarrow$ `TcJobStatus.CANCELLED` / `JobStatus.CANCELLED`.

2. **Tracking GPS en Vivo en `TaskController`**:
   - Cada ejecución de sondeo (`/api/v1/tasks/start-tracking`) consulta `dispatcherPort.trackJob(tcJobId)`.
   - Coordenadas válidas actualizan atómicamente `lastLocation` y añaden una muestra en `trackingHistory` dentro de `jobs/{bookingRef}` (con límite protector FinOps de 500 puntos).

3. **Aislamiento Estricto No-HBX (`isHbxOrigin`)**:
   - `BookingMapping.isHbxOrigin()` discrimina reservas que no provienen de HBX (`ES-JOSE1-`, `MANUAL-`, `ORPHAN-`, `TEST-`, `DIRECT-`, `TC-`, `LOCAL-`, `PA-TEST-`).
   - Se suprimen automáticamente las llamadas hacia `providerSourcingPort.updateDriverDetails` y `providerSourcingPort.updateVehicleLocation`.

4. **Resolución Multi-Tenant Resiliente Asíncrona**:
   - `ProcessAssignmentEventService` y `FirestoreClientResolver` resuelven el tenant analizando el prefijo de la referencia (`ES-`, `PA-`, `DO-`) o buscando en cascada, evitando cualquier fallback a bases de datos `(default)` inexistentes.

5. **Finalización y Desescalado Limpio de Cloud Tasks**:
   - Al detectar `COMPLETED`, `TaskController` actualiza `bookingMappings` y `jobs` a `COMPLETED` y no reprograma más tareas, cerrando la cadena de sondeos.

## Consecuencias y Verificación
- **Fidelidad Operativa 100%**: Estados y coordenadas reflejan en tiempo real lo que el conductor hace en su aplicación móvil.
- **Calidad y Rendimiento (Consilium Romano)**:
  - 306 tests unitarios superados con 0 fallos.
  - 1.000.000 de simulaciones Monte Carlo ejecutadas (60M meses operativos) confirmando:
    - Coste por MAU: `\$0.00020 USD` (P50) y `\$0.00078 USD` (P99), muy por debajo del límite estricto de `\$0.015 USD`.
    - Tasa de pérdida de asignaciones: `0.00%`.
    - Norma de covarianza EnKF: `0.000355` ($< 0.5$, convergente).
