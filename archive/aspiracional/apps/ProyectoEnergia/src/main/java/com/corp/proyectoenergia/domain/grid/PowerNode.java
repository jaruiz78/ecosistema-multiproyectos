package com.corp.proyectoenergia.domain.grid;
/**
 * Arquitectura y especificación formal para PowerNode.
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 */
public record PowerNode(String nodeId, double generationCapacity, double currentLoad) {
    public double calculateReserve() {
        return generationCapacity - currentLoad;
    }
}
