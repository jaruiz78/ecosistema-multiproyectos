package com.corp.ecosystem.soilbiocarbon.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: SoilCarbonParcelTwin (Microbioma de Suelos y Secuestro de Carbono Agrícola / MRV).
 * <p>
 * Modela el secuestro orgánico de carbono en suelo (Soil Organic Carbon - SOC) integrando metagenómica
 * de hongos micorrízicos/bacterias fijadoras de nitrógeno y sensores de respiración edáfica sobre malla H3.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference FAO Global Soil Organic Carbon (GSOC); Verra VM0042 Soil Carbon Quantification Methodology
 */
public record SoilCarbonParcelTwin(
        ParcelId id,
        String tenantId,
        long h3IndexRes8,
        double surfaceHectares,
        SoilMetagenomicProfile metagenomics,
        CarbonSequestrationBalance balance,
        CarbonCreditEligibilityStatus eligibilityStatus,
        Instant lastSampledAt
) implements Serializable {

    public record ParcelId(String value) {
        public ParcelId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("ParcelId no puede estar vacío");
        }
    }

    public record SoilMetagenomicProfile(
            double mycorrhizalFungiRatio,
            double microbialBiomassCarbonMgPerKg,
            double soilRespirationFluxUmolsM2s
    ) {}

    public record CarbonSequestrationBalance(
            double baselineSocPercentage,
            double currentSocPercentage,
            double netSequestrationTonsCo2ePerYear
    ) {}

    public enum CarbonCreditEligibilityStatus {
        VERRA_VM0042_ELIGIBLE, SAMPLING_IN_PROGRESS, DEFICIT_SOIL_DEPLETED
    }

    public static SoilCarbonParcelTwin evaluateParcel(
            ParcelId id,
            String tenantId,
            long h3Index,
            double hectares,
            SoilMetagenomicProfile metagenomics,
            double baselineSoc,
            double currentSoc
    ) {
        // Cálculo estequiométrico de CO2e secuestrado: deltaSoc * BulkDensity (1.3 t/m3) * Depth (0.3m) * 44/12 * 10,000 m2
        double deltaSocPct = Math.max(0.0, currentSoc - baselineSoc);
        double netTonsCo2e = (deltaSocPct / 100.0) * 1.3 * 0.3 * (44.0 / 12.0) * 10000.0 * hectares;

        CarbonSequestrationBalance balance = new CarbonSequestrationBalance(baselineSoc, currentSoc, netTonsCo2e);

        CarbonCreditEligibilityStatus status = CarbonCreditEligibilityStatus.SAMPLING_IN_PROGRESS;
        if (netTonsCo2e > (1.5 * hectares) && metagenomics.mycorrhizalFungiRatio() >= 0.25) {
            status = CarbonCreditEligibilityStatus.VERRA_VM0042_ELIGIBLE;
        } else if (netTonsCo2e <= 0.0) {
            status = CarbonCreditEligibilityStatus.DEFICIT_SOIL_DEPLETED;
        }

        return new SoilCarbonParcelTwin(
                id,
                tenantId,
                h3Index,
                hectares,
                metagenomics,
                balance,
                status,
                Instant.now()
        );
    }
}
