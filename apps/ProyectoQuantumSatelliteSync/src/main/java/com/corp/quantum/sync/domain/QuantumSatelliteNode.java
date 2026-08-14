package com.corp.quantum.sync.domain;

/**
 * Representa un satélite en órbita baja terrestre (LEO) equipado con reloj atómico óptico
 * y transmisor de fotones entrelazados para distribución de claves cuánticas (QKD).
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record QuantumSatelliteNode(
        String satelliteId,
        double altitudeKm,
        double orbitalVelocityKmh,
        double atomicClockDriftPicoseconds,
        double qkdKeyRateBitsPerSec,
        double quantumBitErrorRateQber
) {
    public QuantumSatelliteNode {
        if (satelliteId == null || satelliteId.isBlank()) {
            throw new IllegalArgumentException("satelliteId no puede estar vacío");
        }
        if (altitudeKm < 150.0 || altitudeKm > 2000.0) {
            throw new IllegalArgumentException("Altitud orbital LEO debe estar entre 150 km y 2000 km");
        }
        if (quantumBitErrorRateQber < 0.0 || quantumBitErrorRateQber > 1.0) {
            throw new IllegalArgumentException("QBER debe estar entre 0.0 y 1.0");
        }
    }

    public boolean isQkdLinkSecure() {
        // En protocolos BB84/E91, si el QBER es inferior al 11%, se garantiza que no hay eavesdropping
        return quantumBitErrorRateQber < 0.11;
    }
}
