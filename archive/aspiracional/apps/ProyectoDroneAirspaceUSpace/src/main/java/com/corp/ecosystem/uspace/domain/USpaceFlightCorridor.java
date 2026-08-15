package com.corp.ecosystem.uspace.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: USpaceFlightCorridor (Movilidad Aérea Urbana / Gestión U-Space de Drones).
 * <p>
 * Gestiona corredores aéreos 3D sobre mallas Uber H3 y desconflicción temporal de trayectorias (4D)
 * para drones de entrega médica/paquetería y eVTOLs (aerotaxis).
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference EASA U-Space Regulatory Framework (EU) 2021/664; ASTM F3411 Remote ID
 */
public record USpaceFlightCorridor(
        FlightPlanId id,
        String tenantId,
        String droneOperatorId,
        List<AirspaceWaypoint3D> flightPath,
        AltitudeLayer altitudeLayer,
        FlightDeconflictionStatus deconflictionStatus,
        Instant departureTime,
        Instant estimatedArrivalTime
) implements Serializable {

    public record FlightPlanId(String value) {
        public FlightPlanId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("FlightPlanId no puede estar vacío");
        }
    }

    public record AirspaceWaypoint3D(
            long h3IndexRes9,
            double altitudeMetersAgl,
            long expectedArrivalEpochMs
    ) {}

    public enum AltitudeLayer {
        VERY_LOW_LEVEL_0_50M, URBAN_DELIVERY_50_120M, EVTOL_CORRIDOR_120_300M
    }

    public enum FlightDeconflictionStatus {
        STRATEGIC_DECONFLICTED_CLEAR, CONFLICT_DETECTED_REROUTING, EMERGENCY_HOLD
    }

    public static USpaceFlightCorridor authorizeFlightPlan(
            FlightPlanId id,
            String tenantId,
            String operatorId,
            List<AirspaceWaypoint3D> path,
            AltitudeLayer layer,
            boolean hasSpatialConflict
    ) {
        FlightDeconflictionStatus status = hasSpatialConflict ?
                FlightDeconflictionStatus.CONFLICT_DETECTED_REROUTING :
                FlightDeconflictionStatus.STRATEGIC_DECONFLICTED_CLEAR;

        return new USpaceFlightCorridor(
                id,
                tenantId,
                operatorId,
                List.copyOf(path),
                layer,
                status,
                Instant.now(),
                Instant.now().plusSeconds(600)
        );
    }
}
