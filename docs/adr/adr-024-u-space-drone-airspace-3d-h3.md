# ADR-024: Gestión de Espacio Aéreo U-Space y Detección de Conflictos 4D con H3

## Estado
Aprobado - Agosto 2026

## Contexto
La integración de operaciones con drones comerciales y eVTOL requiere segregación espacial dinámica en 3 dimensiones más tiempo (4D), resolución determinista de conflictos en espacio aéreo bajo (Very Low Level Airspace - VLL) y cero latencia de cálculo para evitar colisiones.

## Decisión
1. Crear el vertical `ProyectoDroneAirspace` con el servicio `UspaceConflictResolutionService`.
2. Extender la indexación hexagonal 2D de Uber H3 hacia prismas volumétricos 3D (H3 Index + Estrato de Altitud en metros AGL) con granularidad temporal en segundos.
3. Modelar las trayectorias de vuelo como secuencias de envolventes cilíndricas 4D y aplicar algoritmos de resolución determinista de conflictos con separación mínima de seguridad de \(50\text{ m}\) horizontal y \(30\text{ m}\) vertical.

## Consecuencias
- **Positivas**: Resolución de colisiones en tiempo \(O(N)\) por celda H3, compatibilidad con los reglamentos europeos U-Space (EU 2021/664) y FAA UTM.
- **Negativas**: Mayor volumen de telemetría de posición que debe ser ingerida vía micro-batching.
