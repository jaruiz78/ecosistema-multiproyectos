# AGENTS.md - Proyecto Independiente ProyectoSalud (Google Antigravity)

Este proyecto opera como un vertical especializado de alta calidad dentro del ecosistema Multi-Proyecto de Google Antigravity.

## 1. Mapeo de Intención a Skill (SDLC 6-Phase Dispatch)
Cada vez que se reciba un requerimiento u objetivo para este vertical:
- **Nueva Funcionalidad:** `spec-driven-development` -> `planning-and-task-breakdown` -> `incremental-implementation` -> `zero-mockito-tdd-engineer` -> `code-review-and-quality` -> `slsa-sigstore-release-sentinel`
- **Compilación AOT & Leyden CDS:** `leyden-aot-build-master`
- **Diseño de APIs & Puertos:** `api-and-interface-design`
- **Bugs o Refactorización:** `debugging-and-error-recovery` -> `doubt-driven-development`
- **Auditoría Pre-Merge & Senado:** `@code-reviewer`, `@Zero-Trust-Security-Auditor`, `@test-engineer` -> `Consilium Romano 3.0`

## 2. Reglas del Proyecto y Trazabilidad Documental
1. **Arquitectura Hexagonal Pura:** Cero dependencias de infraestructura en `domain/`.
2. **Java 25 & Virtual Threads:** Uso de Records inmutables y `ReentrantLock` para evitar el *Carrier Thread Pinning*.
3. **Parent Dependency:** Hereda de [`corp-spring-boot-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter).
4. **Grounded Javadoc Obligatorio:** Toda clase o record debe incluir `@see apps/VERTICALS_ARCHITECTURE_SPEC.md` y `@see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md`.
5. **Testing Estricto:** Zero-Mockito con JUnit 5 & Testcontainers.

## 3. Especificación Técnica
👉 Consulte: [`apps/VERTICALS_ARCHITECTURE_SPEC.md`](file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md)
