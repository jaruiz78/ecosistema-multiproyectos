package com.proyecto.catastrofes.domain;

/**
 * Modelo de dominio puro para un nodo espacial de zona de evacuación en caso de desastre.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record EvacuationZoneNode(
        String zoneId,
        String h3Cell,
        int currentEvacueeCount,
        double riskLevelPercent,
        boolean routeBlocked
) {
    public EvacuationZoneNode {
        if (zoneId == null || zoneId.isBlank()) {
            throw new IllegalArgumentException("zoneId no puede ser nulo o vacío");
        }
        if (riskLevelPercent < 0 || riskLevelPercent > 100) {
            throw new IllegalArgumentException("El porcentaje de riesgo debe ser entre 0 y 100%");
        }
    }

    public EvacuationZoneNode withEvacuationStep(int evacuatedCount, boolean blocked) {
        int remaining = Math.max(0, currentEvacueeCount - evacuatedCount);
        return new EvacuationZoneNode(zoneId, h3Cell, remaining, riskLevelPercent, blocked);
    }
}
