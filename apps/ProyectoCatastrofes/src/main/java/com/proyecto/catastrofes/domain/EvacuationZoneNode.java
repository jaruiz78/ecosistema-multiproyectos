package com.proyecto.catastrofes.domain;

/**
 * Modelo de dominio puro para un nodo espacial de zona de evacuación en caso de desastre.
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
