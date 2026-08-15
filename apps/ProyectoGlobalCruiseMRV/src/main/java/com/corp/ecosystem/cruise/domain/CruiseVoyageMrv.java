package com.corp.ecosystem.cruise.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: CruiseVoyageMrv (Descarbonización de Flotas de Cruceros y Cumplimiento FuelEU Maritime 2026).
 */
public record CruiseVoyageMrv(
        VoyageId id,
        String tenantId,
        String vesselImoNumber,
        String cruiseLineName,
        FuelMetrics fuelMetrics,
        PortCallEmissionProfile portCall,
        ComplianceStatus status,
        Instant lastRecordedAt
) implements Serializable {

    public record VoyageId(String value) {
        public VoyageId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("VoyageId no puede estar vacío");
        }
    }

    public record FuelMetrics(
            double lngConsumptionTons,
            double greenMethanolTons,
            double mgoConsumptionTons,
            double ghgIntensityGco2PerMj
    ) {
        public boolean isFuelEuCompliant2026() {
            return ghgIntensityGco2PerMj <= 89.34; // Límite FuelEU Maritime 2026 (-2% vs 91.16)
        }
    }

    public record PortCallEmissionProfile(
            String destinationPortUnlocode,
            boolean isColdIroningOnshorePowerUsed,
            double portEmissionsKgCo2
    ) {}

    public enum ComplianceStatus {
        FUELEU_MARITIME_COMPLIANT, EMISSION_PENALTY_SURCHARGE, COLD_IRONING_MANDATORY_CALL
    }

    public static CruiseVoyageMrv recordVoyage(
            VoyageId id,
            String tenantId,
            String imo,
            String lineName,
            FuelMetrics fuel,
            PortCallEmissionProfile port
    ) {
        ComplianceStatus status = fuel.isFuelEuCompliant2026() ?
                ComplianceStatus.FUELEU_MARITIME_COMPLIANT :
                ComplianceStatus.EMISSION_PENALTY_SURCHARGE;

        return new CruiseVoyageMrv(
                id,
                tenantId,
                imo,
                lineName,
                fuel,
                port,
                status,
                Instant.now()
        );
    }
}
