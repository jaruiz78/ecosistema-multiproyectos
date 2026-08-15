# ADR 001: Domain Architecture for ProyectoBioAgriTrace
**Date:** 2026-08-15
**Status:** Approved by Consilium Romano

## Context
Se requiere cerrar la brecha funcional de `ProyectoBioAgriTrace` implementando lógica asintótica en O(1) u O(N log N) e inyección de arquitectura hexagonal pura.

## Decision
1. **Zero-Mockito**: Las pruebas de integración no utilizarán mocks; la capa de dominio está aislada y se usarán Records de Java 25.
2. **Virtual Threads**: Para prevención de Carrier Thread Pinning, los bloqueos stateful se resuelven mediante `ReentrantLock` y no `synchronized`.

## Consequences
Código completamente compatible con GraalVM AOT nativo, Cold-Starts ultra rápidos en Cloud Run y nula entropía en concurrencia masiva.
