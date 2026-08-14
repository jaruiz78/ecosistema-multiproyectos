package com.corp.proyectologistica.domain.spatial;
/**
 * Arquitectura y especificación formal para H3GeoIndex.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public record H3GeoIndex(String h3CellId, double demandSurgeFactor) {
    public double calculateDynamicPricing(double baseFare) {
        return baseFare * demandSurgeFactor;
    }
}
