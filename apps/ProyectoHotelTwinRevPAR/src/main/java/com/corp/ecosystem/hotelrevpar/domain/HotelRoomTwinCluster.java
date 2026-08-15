package com.corp.ecosystem.hotelrevpar.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: HotelRoomTwinCluster (Total RevPAR & Gemelo Digital de Eficiencia Energética Hotelera).
 * <p>
 * Unifica el Revenue Management dinámico con la gestión predictiva de HVAC y agua caliente
 * sanitaria (DHW) de habitaciones hoteleras, optimizando el GOPPAR neto.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference Cornell Center for Hospitality Research (Total RevPAR / GOPPAR Models)
 */
public record HotelRoomTwinCluster(
        HotelId id,
        String tenantId,
        String hotelName,
        int totalRooms,
        RoomInventoryState inventoryState,
        EnergyThermalProfile thermalProfile,
        DynamicPricingStrategy pricingStrategy,
        Instant lastOptimizedAt
) implements Serializable {

    public record HotelId(String value) {
        public HotelId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("HotelId no puede estar vacío");
        }
    }

    public record RoomInventoryState(
            int occupiedRooms,
            int preCheckinAssignedRooms,
            int availableRooms,
            double averageDailyRateEur
    ) {
        public double occupancyRate(int total) {
            return (double) occupiedRooms / total;
        }

        public double calculateRevPar(int total) {
            return occupancyRate(total) * averageDailyRateEur;
        }
    }

    public record EnergyThermalProfile(
            double outdoorTempCelsius,
            double averageIndoorTempCelsius,
            double currentHvacPowerKw,
            double energyCostPerKwhEur
    ) {}

    public record DynamicPricingStrategy(
            BigDecimal baseRateEur,
            BigDecimal currentSuggestedRateEur,
            double demandSurgeMultiplier,
            boolean isPreCoolingActive
    ) {}

    public HotelRoomTwinCluster optimizePricingAndHvac(
            int expectedArrivalsNext3Hours,
            double outdoorTempCelsius,
            double localH3DemandIndex
    ) {
        // Cálculo de tarifa dinámica
        double surge = 1.0;
        if (localH3DemandIndex > 1.5) surge += 0.25;
        if (expectedArrivalsNext3Hours > (totalRooms * 0.2)) surge += 0.15;

        BigDecimal suggestedRate = pricingStrategy.baseRateEur.multiply(BigDecimal.valueOf(surge));

        // Decisión de pre-climatización (MPC)
        boolean triggerPreCool = (outdoorTempCelsius > 30.0 || outdoorTempCelsius < 10.0) && expectedArrivalsNext3Hours > 0;
        double nextHvacKw = triggerPreCool ? (expectedArrivalsNext3Hours * 1.2) : (inventoryState.occupiedRooms * 0.8);

        RoomInventoryState nextInventory = new RoomInventoryState(
                inventoryState.occupiedRooms,
                expectedArrivalsNext3Hours,
                Math.max(0, totalRooms - inventoryState.occupiedRooms - expectedArrivalsNext3Hours),
                suggestedRate.doubleValue()
        );

        EnergyThermalProfile nextThermal = new EnergyThermalProfile(
                outdoorTempCelsius,
                22.5,
                nextHvacKw,
                thermalProfile.energyCostPerKwhEur
        );

        DynamicPricingStrategy nextStrategy = new DynamicPricingStrategy(
                pricingStrategy.baseRateEur,
                suggestedRate,
                surge,
                triggerPreCool
        );

        return new HotelRoomTwinCluster(
                this.id,
                this.tenantId,
                this.hotelName,
                this.totalRooms,
                nextInventory,
                nextThermal,
                nextStrategy,
                Instant.now()
        );
    }
}
