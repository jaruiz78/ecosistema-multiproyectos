package com.corp.proyectob2g.domain.automata;
/**
 * Arquitectura y especificación formal para CellularState.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public record CellularState(String cellId, int saturationLevel) {
    public CellularState propagate(int externalLoad) {
        return new CellularState(cellId, Math.min(100, saturationLevel + externalLoad));
    }
}
