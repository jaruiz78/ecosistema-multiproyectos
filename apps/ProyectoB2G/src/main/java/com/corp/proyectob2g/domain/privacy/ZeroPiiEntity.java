package com.corp.proyectob2g.domain.privacy;
/**
 * Arquitectura y especificación formal para ZeroPiiEntity.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public record ZeroPiiEntity(String anonymizedId, String region) {
    public boolean isValid() { return anonymizedId != null && anonymizedId.startsWith("anon_"); }
}
