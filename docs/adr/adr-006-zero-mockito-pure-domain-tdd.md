# ADR-006: Estándar Zero-Mockito, Stubs Herméticos y TDD en Dominio Puro

## Estado
Aceptado

## Contexto
El ecosistema multi-proyecto requiere garantizar pruebas unitarias y de integración de máxima fidelidad empírica sin acoplamiento a frameworks de mockeo dinámico (como Mockito). Los mocks tradicionales introducen fragilidad ante refactorizaciones, ocultan comportamientos concurrentes reales y no validan invariantes de estado bajo Virtual Threads (Project Loom).

## Decisión
1. **Zero-Mockito Obligatorio**: Prohibir terminantemente `@Mock`, `@MockBean` o `Mockito.mock()` en capas de dominio puro (`domain/`) y de aplicación (`application/`).
2. **Stubs In-Memory Deterministas**: Implementar adaptadores herméticos en memoria (ej. `InMemory*RepositoryAdapter` con `ConcurrentHashMap`) implementando los puertos de salida (`*RepositoryPort`).
3. **Validación AST**: Ejecutar `ArchUnit` o linters estáticos en los builds de CI para bloquear dependencias transitivas de testing sobre librerías de byte-buddy/mockito en el dominio.
4. **Testcontainers para Infraestructura**: Las pruebas que requieran persistencia real o PostgreSQL RLS deben utilizar contenedores reales mediante Testcontainers.

## Consecuencias
- **Positivas**: Pruebas 100% deterministas, ejecución ultra-rápida en memoria (<50 ms por suite), total compatibilidad con AOT/Leyden y Virtual Threads.
- **Trade-offs**: Requiere escribir explícitamente clases de prueba in-memory que implementen los puertos del dominio.

## Referencias
- Evans (2003) Domain-Driven Design.
- Martin (2017) Clean Architecture.
- Benchmark: CMU / Stanford Software Engineering Standard.
