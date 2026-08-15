package com.corp.ecosystem.dti.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: SmartDestinationZone (DTI / UNE 178 / Capacidad de Carga Turística).
 * <p>
 * Modela zonas turísticas discretizadas en celdas Uber H3 (playas, cascos históricos, monumentos),
 * calculando en tiempo real el Índice de Capacidad de Carga Turística (TCC) y activando
 * protocolos de dispersión peatonal y reencauzamiento de autobuses lanzadera.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference UNE 178501 (Sistema de Gestión de Destinos Turísticos Inteligentes); UNWTO Tourism Carrying Capacity
 */
public record SmartDestinationZone(
        ZoneId id,
        String tenantId,
        String destinationName,
        long h3IndexRes8,
        ZoneType type,
        CarryingCapacityLimits limits,
        CurrentCrowdState currentState,
        List<DispersionRoute> alternativeRoutes,
        ZoneAlertLevel alertLevel,
        Instant lastAssimilatedAt
) implements Serializable {

    public record ZoneId(String value) {
        public ZoneId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("ZoneId no puede estar vacío");
        }
    }

    public enum ZoneType {
        HISTORIC_CENTER, BEACH_COASTAL, NATURAL_PARK, MONUMENT_HERITAGE, CRUISE_PORT_TERMINAL
    }

    public record CarryingCapacityLimits(
            int maxSimultaneousVisitors,
            double maxPcuPerKm2, // Passenger Car Units
            double maxInstantNoiseDb,
            double criticalOccupancyRatio
    ) {}

    public record CurrentCrowdState(
            int estimatedVisitors,
            double noiseLevelDb,
            double pedestrianDensityRatio,
            int activeShuttleBuses
    ) {
        public double occupancyRatio(int maxVisitors) {
            return (double) estimatedVisitors / maxVisitors;
        }
    }

    public record DispersionRoute(
            String routeId,
            String targetPoiName,
            long targetH3IndexRes8,
            int discountVoucherPct,
            int estimatedWalkMinutes
    ) {}

    public enum ZoneAlertLevel {
        GREEN_OPTIMAL, YELLOW_PRE_SATURATION, ORANGE_DISPERSION_ACTIVE, RED_CAPACITY_EXCEEDED
    }

    public SmartDestinationZone assimilateCrowdObservation(int obsVisitors, double obsNoiseDb) {
        double ratio = (double) obsVisitors / limits.maxSimultaneousVisitors();

        ZoneAlertLevel nextAlert = ZoneAlertLevel.GREEN_OPTIMAL;
        int nextBuses = currentState.activeShuttleBuses();

        if (ratio >= 1.0) {
            nextAlert = ZoneAlertLevel.RED_CAPACITY_EXCEEDED;
            nextBuses = Math.max(nextBuses, 6);
        } else if (ratio >= limits.criticalOccupancyRatio()) { // ej: 0.85
            nextAlert = ZoneAlertLevel.ORANGE_DISPERSION_ACTIVE;
            nextBuses = Math.max(nextBuses, 4);
        } else if (ratio >= 0.70) {
            nextAlert = ZoneAlertLevel.YELLOW_PRE_SATURATION;
            nextBuses = Math.max(nextBuses, 2);
        }

        CurrentCrowdState nextState = new CurrentCrowdState(obsVisitors, obsNoiseDb, ratio, nextBuses);

        return new SmartDestinationZone(
                this.id,
                this.tenantId,
                this.destinationName,
                this.h3IndexRes8,
                this.type,
                this.limits,
                nextState,
                this.alternativeRoutes,
                nextAlert,
                Instant.now()
        );
    }
}
