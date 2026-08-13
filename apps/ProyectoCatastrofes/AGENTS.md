# AGENTS.md - Proyecto Independiente ProyectoCatastrofes (Google Antigravity)

Este proyecto opera como un vertical independiente de Orquestación de Evacuación en Tiempo Real y Gestión de Resiliencia ante Catástrofes (`ResilientCivTech`) dentro del ecosistema Multi-Proyecto.

## 1. Mapeo de Intención a Skill
- **Nueva Funcionalidad**: `spec-driven-development` -> `incremental-implementation` -> `test-driven-development`
- **Diseño de APIs**: `api-and-interface-design`
- **Auditoría de Código**: `code-review-and-quality` (`@code-reviewer`)
- **Seguridad & Zero-Trust**: `security-and-hardening` (`@security-auditor`)

## 2. Reglas del Proyecto
- **Arquitectura Hexagonal Pura**: Cero dependencias de infraestructura en `domain/`.
- **Java 25 & Virtual Threads**: ReentrantLock anti-pinning.
- **Parent Dependency**: Hereda de `corp-spring-boot-starter`.
- **Testing**: Zero-Mockito con JUnit 5 & Testcontainers.
