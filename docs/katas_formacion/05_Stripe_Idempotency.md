# Kata 05: Stripe Idempotency & Sagas

## Objetivo
Asegurar transacciones idempotentes para pasarelas de pago Fintech.

## Reglas
- Mandar cabecera `Idempotency-Key`.
- Sagas / Outbox Pattern para consistencia transaccional eventual.
