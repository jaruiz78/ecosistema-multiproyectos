# ADR-007: Aislamiento Multi-Tenant de Triple Capa con ScopedValues y PostgreSQL RLS

## Estado
Aceptado

## Contexto
En un entorno SaaS multi-tenant con Virtual Threads, los `ThreadLocal` tradicionales pueden provocar fugas de memoria o retención excesiva de contexto al ser reutilizados por carriers del sistema operativo. Además, la seguridad de datos exige que el aislamiento no dependa exclusivamente del código de aplicación, sino que se imponga forzosamente en el motor de base de datos relacional.

## Decisión
1. **Runtime Context**: Utilizar `ScopedValue<String>` nativo de Java 25 (`HardwareIsolatedTenantContext` / `TenantContext`) para propagar el `tenantId` de forma inmutable y con scope delimitado a la ejecución de la tarea.
2. **Capa ORM / Hibernate**: Aplicar filtros automáticos de sesión `@Filter(name = "tenantFilter")` en entidades JPA que extienden de `BaseTenantEntity`.
3. **Capa de Base de Datos**: Habilitar `FORCE ROW LEVEL SECURITY` en PostgreSQL con políticas `USING (tenant_id = current_setting('app.current_tenant', true))` inyectadas dinámicamente en `MultiTenantConnectionProviderImpl`.

## Consecuencias
- **Positivas**: Cero fugas de datos entre tenants (Zero-Trust Triple Layer), coste $O(1)$ en propagación de contexto, inmutabilidad garantizada por el runtime JVM.
- **Trade-offs**: Requiere que todas las conexiones de pool configuren explícitamente `SET app.current_tenant` antes de ejecutar transacciones.

## Referencias
- Java 25 Scoped Values (JEP 446 / 487).
- PostgreSQL Documentation: Row Level Security (RLS).
- BeyondCorp: Zero-Trust Architecture (Google Research).
