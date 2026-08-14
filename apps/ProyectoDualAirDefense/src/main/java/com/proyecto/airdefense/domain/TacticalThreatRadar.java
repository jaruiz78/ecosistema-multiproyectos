package com.proyecto.airdefense.domain;

import java.util.Objects;

/**
 * Modelo de dominio puro para Detección Táctica de Amenazas Aéreas y Marítimas.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record TacticalThreatRadar(
        String targetTrackId,
        String h3HexCell,
        double altitudeMeters,
        double velocityMetersPerSec,
        double radarCrossSectionSqm,
        double acousticSignatureDb,
        boolean isHostileSignature
) {
    public TacticalThreatRadar {
        Objects.requireNonNull(targetTrackId, "targetTrackId no puede ser nulo");
        Objects.requireNonNull(h3HexCell, "h3HexCell no puede ser nulo");
        if (altitudeMeters < -100 || velocityMetersPerSec < 0) {
            throw new IllegalArgumentException("Altitud y velocidad deben ser parámetros físicos válidos");
        }
    }

    public int computeThreatPriorityLevel() {
        if (!isHostileSignature) return 0;
        int level = 1;
        if (velocityMetersPerSec > 250.0) level += 2; // > 900 km/h
        if (radarCrossSectionSqm < 0.1) level += 1;  // Stealth profile
        if (altitudeMeters < 500.0) level += 1;      // Low altitude penetration
        return Math.min(5, level);
    }
}
