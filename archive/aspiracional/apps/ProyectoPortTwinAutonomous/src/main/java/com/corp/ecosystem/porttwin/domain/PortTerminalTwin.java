package com.corp.ecosystem.porttwin.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: PortTerminalTwin (Gemelo Digital de Terminales Portuarias y Grúas STS).
 * <p>
 * Optimiza la asignación de atraques (Berth Allocation Problem - BAP) y el despacho de grúas STS
 * y vehículos guiados automáticamente (AGVs) para minimizar el tiempo de estadía de buques porta-contenedores.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference IMO Maritime Autonomous Surface Ships (MASS); Port of Rotterdam / Singapore Smart Port Standards
 */
public record PortTerminalTwin(
        TerminalId id,
        String tenantId,
        String portUnLoCode,
        List<BerthQuay> berths,
        YardStorageState yardState,
        CraneDispatchSchedule currentSchedule,
        Instant lastOptimizedAt
) implements Serializable {

    public record TerminalId(String value) {
        public TerminalId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("TerminalId no puede estar vacío");
        }
    }

    public record BerthQuay(
            String berthCode,
            double maxDraftMeters,
            double lengthMeters,
            int assignedStsCranesCount,
            boolean isOccupied
    ) {}

    public record YardStorageState(
            int currentTeuOccupancy,
            int maxTeuCapacity,
            double yardCongestionIndexPct
    ) {}

    public record CraneDispatchSchedule(
            String vesselImoNumber,
            String assignedBerthCode,
            int movesPerHourExpected,
            double estimatedTurnaroundHours
    ) {}

    public PortTerminalTwin scheduleVesselBerthing(String imoNumber, int requiredTeuMoves, double vesselDraftMeters) {
        BerthQuay availableBerth = berths.stream()
                .filter(b -> !b.isOccupied() && b.maxDraftMeters() >= vesselDraftMeters)
                .findFirst()
                .orElse(berths.getFirst());

        int cranes = Math.max(2, availableBerth.assignedStsCranesCount());
        int movesPerHour = cranes * 28; // ~28 movimientos/hora/grúa
        double turnaroundHours = (double) requiredTeuMoves / movesPerHour;

        CraneDispatchSchedule schedule = new CraneDispatchSchedule(
                imoNumber, availableBerth.berthCode(), movesPerHour, turnaroundHours
        );

        List<BerthQuay> nextBerths = berths.stream()
                .map(b -> b.berthCode().equals(availableBerth.berthCode()) ?
                        new BerthQuay(b.berthCode(), b.maxDraftMeters(), b.lengthMeters(), cranes, true) : b)
                .toList();

        return new PortTerminalTwin(
                this.id,
                this.tenantId,
                this.portUnLoCode,
                nextBerths,
                this.yardState,
                schedule,
                Instant.now()
        );
    }
}
