# AGENTS.md - Core Algorítmico core-formal-verification (Google Antigravity)

Módulo algorítmico puro del Gemelo Digital Unificado. Opera como biblioteca de verificación formal y lógica de Hoare sin persistencia directa.

## 1. Mapeo de Intención a Skill (SDLC 6-Phase Dispatch)
- **Nueva Funcionalidad:** `spec-driven-development` -> `planning-and-task-breakdown` -> `incremental-implementation` -> `zero-mockito-tdd-engineer` -> `code-review-and-quality`
- **Compilación AOT & Leyden CDS:** `leyden-aot-build-master`
- **Verificación Formal y Consenso:** `formal_verification_architect` -> `consilium_romano_architect`
- **Bugs o Refactorización:** `debugging-and-error-recovery` -> `doubt-driven-development`

## 2. Reglas del Módulo
1. **Biblioteca Algorítmica Pura:** Cero dependencias de infraestructura en `domain/`. Sin persistencia directa.
2. **Java 25 & Virtual Threads:** Records inmutables, `ReentrantLock` para mitigar Carrier Thread Pinning.
3. **Parent Dependency:** Hereda de [`corp-spring-boot-starter`](file:///home/jaruiz/Desarrollo/corp-spring-boot-starter).
4. **Grounded Javadoc Obligatorio:** `@see docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md` y `@see docs/formacion_ecosistema/BIBLIOGRAFIA_ACADEMICA.md`.
5. **Testing Estricto:** Zero-Mockito con JUnit 5. Validación de preservación inductiva e invariantes de seguridad/liveness obligatorios.

## 3. Integración con Gemelo Digital
- Certifica las invariantes de conservación de masa/energía en `ProyectoHidrogeno`, `ProyectoVPP` y las reglas de no colisión en `ProyectoDroneAirspace`.
