package com.corp.agro.robotics.domain;

/**
 * Representa un micro-drone o robot terrestre en un enjambre agro-biológico.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public record BioDroneSwarmNode(
        String droneId,
        String h3CellIndexHex,
        double altitudeMeters,
        double batteryPercent,
        double payloadGram,
        boolean pollinationActive,
        int flowersPollinatedCount
) {
    public BioDroneSwarmNode {
        if (droneId == null || droneId.isBlank()) {
            throw new IllegalArgumentException("droneId no puede estar vacío");
        }
        if (batteryPercent < 0.0 || batteryPercent > 100.0) {
            throw new IllegalArgumentException("Batería debe estar entre 0% y 100%");
        }
    }

    public boolean canDeployMission() {
        return batteryPercent >= 20.0;
    }
}
