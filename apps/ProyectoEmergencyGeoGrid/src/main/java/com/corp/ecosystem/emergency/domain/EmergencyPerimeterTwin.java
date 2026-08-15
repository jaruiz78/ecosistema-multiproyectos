package com.corp.ecosystem.emergency.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: EmergencyPerimeterTwin (Protección Civil / Gemelo Digital de Incendios e Inundaciones).
 * <p>
 * Modela la propagación de frentes de fuego (modelo de Rothermel) o desbordamientos fluviales
 * sobre malla hexagonal Uber H3, calculando órdenes de evacuación y recursos asignados en tiempo real.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference Rothermel (1972) Fire Spread Model; Copernicus Emergency Management Service (EMS)
 */
public record EmergencyPerimeterTwin(
        EmergencyId id,
        String tenantId,
        EmergencyType type,
        List<Long> activeH3CellsRes8,
        MeteorologicalVector weather,
        EvacuationAssessment evacuation,
        EmergencyLevel level,
        Instant lastSpreadCalculationAt
) implements Serializable {

    public record EmergencyId(String value) {
        public EmergencyId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("EmergencyId no puede estar vacío");
        }
    }

    public enum EmergencyType {
        WILDFIRE_FOREST, FLASH_FLOOD, INDUSTRIAL_CHEMICAL_LEAK
    }

    public record MeteorologicalVector(
            double windSpeedKmH,
            double windDirectionDegrees,
            double ambientTemperatureCelsius,
            double relativeHumidityPct
    ) {}

    public record EvacuationAssessment(
            int estimatedAffectedPopulation,
            List<Long> recommendedEvacuationH3CellsRes8,
            int deployedFirefightingUnits,
            boolean isAirSupportRequested
    ) {}

    public enum EmergencyLevel {
        SITUATION_1_LOCAL, SITUATION_2_REGIONAL, SITUATION_3_NATIONAL_UME
    }

    public EmergencyPerimeterTwin calculateNextSpread(
            double nextWindSpeedKmH,
            double nextWindDir,
            List<Long> adjacentH3IgnitedCells
    ) {
        MeteorologicalVector nextWeather = new MeteorologicalVector(
                nextWindSpeedKmH, nextWindDir, weather.ambientTemperatureCelsius(), weather.relativeHumidityPct()
        );

        List<Long> nextCells = new java.util.ArrayList<>(this.activeH3CellsRes8);
        for (Long cell : adjacentH3IgnitedCells) {
            if (!nextCells.contains(cell)) nextCells.add(cell);
        }

        EmergencyLevel nextLevel = EmergencyLevel.SITUATION_1_LOCAL;
        int population = nextCells.size() * 350; // Densidad estimada
        boolean airSupport = false;

        if (nextCells.size() > 20 || nextWindSpeedKmH > 50.0) {
            nextLevel = EmergencyLevel.SITUATION_3_NATIONAL_UME;
            airSupport = true;
        } else if (nextCells.size() > 5 || nextWindSpeedKmH > 30.0) {
            nextLevel = EmergencyLevel.SITUATION_2_REGIONAL;
        }

        EvacuationAssessment nextEvac = new EvacuationAssessment(
                population,
                List.copyOf(nextCells),
                nextCells.size() * 2,
                airSupport
        );

        return new EmergencyPerimeterTwin(
                this.id,
                this.tenantId,
                this.type,
                List.copyOf(nextCells),
                nextWeather,
                nextEvac,
                nextLevel,
                Instant.now()
        );
    }
}
