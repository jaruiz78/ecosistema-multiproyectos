# Project: Google-Level Advanced Optimizations Across Multi-Repo Architecture

## Overview
Implementación de optimizaciones avanzadas de nivel Google en los 4 repositorios del ecosistema:
1. `corp-spring-boot-starter`: Starter corporativo Spring Boot 4.1 / Java 25.
2. `SaaSRegantes`: Plataforma de gestión hídrica y subastas multi-tenant.
3. `pctMultiMicroservices`: Sistema de microservicios de movilidad/transporte con Go BFF y Java backend.
4. `AppViajes`: Aplicación móvil/web de movilidad e itinerarios inteligentes.

## Architecture & Scope
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│                           Google-Level Optimizations                            │
├───────────────────────────────┬─────────────────────────────────────────────────┤
│ Repository                    │ Target Optimizations                            │
├───────────────────────────────┼─────────────────────────────────────────────────┤
│ 1. corp-spring-boot-starter   │ - Extensibilidad autoconfig (@ConditionalOnMissingBean) │
│                               │ - Interceptores gRPC / W3C traceparent           │
│                               │ - Compatibilidad Ahead-Of-Time (AOT) & Leyden CDS│
├───────────────────────────────┼─────────────────────────────────────────────────┤
│ 2. SaaSRegantes               │ - Optimización subastas H3 y física (Rust/SIMD  │
│                               │   o Java 25 Vector API)                         │
│                               │ - Ingesta de telemetría IoT estructurada          │
├───────────────────────────────┼─────────────────────────────────────────────────┤
│ 3. pctMultiMicroservices      │ - Contrato gRPC/Protobuf v3 (Go BFF <-> Java)   │
│                               │ - Reutilización de buffers con sync.Pool en Go  │
│                               │ - Segregación persistencia caliente (Redis) /   │
│                               │   fría (Firestore)                              │
├───────────────────────────────┼─────────────────────────────────────────────────┤
│ 4. AppViajes                  │ - IA Híbrida Edge/Cloud (LiteRT + Gemma 2B Edge │
│                               │   / Vertex AI cloud)                            │
│                               │ - Analítica OLAP client-side (DuckDB-WASM/Parquet)│
└───────────────────────────────┴─────────────────────────────────────────────────┘
```

## Milestones & Status

| # | Milestone Name | Target Repositories | Core Deliverables | Status |
|---|----------------|---------------------|-------------------|--------|
| M1 | corp-spring-boot-starter | `corp-spring-boot-starter` | Autoconfig extensible, gRPC W3C tracing, AOT/Leyden training script & .jsa generation | DONE |
| M2 | SaaSRegantes Optimization | `SaaSRegantes` | Subastas H3 optimizadas con SIMD / Vector API, ingesta IoT concurrente | DONE |
| M3 | pctMultiMicroservices Optimization | `pctMultiMicroservices` | gRPC/Protobuf v3, Go sync.Pool, Redis hot cache + Firestore cold persistence | DONE |
| M4 | AppViajes Hybrid AI & OLAP | `AppViajes` | LiteRT + Gemma 2B Edge / Vertex AI fallback, DuckDB-WASM OLAP engine | DONE |
| M5 | Simulations & Docs Update | All repositories | Update simulation scripts & README.md/ARCHITECTURE.md across all 4 repos | DONE |
| M6 | Consilium Romano Audit & Report | All repositories | Verification of stochastic convergence, Zero-Mockito, no Virtual Thread pinning, P95/P99/RAM/bundle/cold-start report | DONE (🟢 Aprobado Consilium Romano) |

## Code Layout & Conventions
- `corp-spring-boot-starter`: `/home/jaruiz/Desarrollo/corp-spring-boot-starter`
- `SaaSRegantes`: `/home/jaruiz/Desarrollo/SaaSRegantes`
- `pctMultiMicroservices`: `/home/jaruiz/Desarrollo/PCT/PCT_TASKS/pctMultiMicroservices`
- `AppViajes`: `/home/jaruiz/Desarrollo/AppViajes`
- Strict Hexagonal Architecture & Zero-Mockito policy in `domain/`.
- Java 25 Virtual Threads execution without carrier thread pinning.

## Verification & Gate Criteria
1. Build & Unit/Integration Tests pass 100%.
2. Code/Architecture Review approved without vetoes.
3. Challenger empirical performance & stress tests pass.
4. Forensic Integrity Auditor verdict is CLEAN (Zero cheating / no dummy mocks).
