# ADR-023: Bus de Mensajería Event Mesh de Ultra-Baja Latencia con NATS JetStream

## Estado
Aprobado - Agosto 2026

## Contexto
Los flujos de telemetría IoT de alta frecuencia (redes de agua de SaaSRegantes, tracking H3 de AppViajes, gestión de microrredes de ProyectoEnergia y control de drones) exigen tiempos de despacho de eventos inferiores a \(100\ \mu\text{s}\) en memoria y desacoplamiento absoluto de infraestructura.

## Decisión
1. Desarrollar `corp-event-mesh-nats-starter` con soporte de publicación y suscripción sobre NATS JetStream y ring buffers circulares lock-free (LMAX Disruptor).
2. Definir contratos de evento inmutables con metadata de tenant (`tenantId`), marcas temporales de nanosegundos y serialización binaria compacta.
3. Proveer fallback transparente a buffers circulares en memoria cuando no se configure un broker NATS externo.

## Consecuencias
- **Positivas**: Latencias de publicación P99 \(< 50\ \mu\text{s}\) en local y \(< 1\text{ ms}\) en clúster; cero contención de hilos con Loom.
- **Negativas**: Requiere despliegue de daemon NATS en entornos distribuidos de producción.
