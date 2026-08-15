package com.corp.proyectologistica.domain.vrp;
/**
 * Arquitectura y especificación formal para RouteRecord.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public record RouteRecord(String routeId, String h3GeoIndex, double priorityScore, boolean isEscrowSettled) {
    public RouteRecord withEscrowSettled(boolean settled) {
        return new RouteRecord(routeId, h3GeoIndex, priorityScore, settled);
    }
}
