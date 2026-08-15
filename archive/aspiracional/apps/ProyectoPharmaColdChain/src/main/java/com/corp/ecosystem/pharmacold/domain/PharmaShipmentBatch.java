package com.corp.ecosystem.pharmacold.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: PharmaShipmentBatch (Logística Farmacéutica GAMP 5 GDP / GLP-1 & Terapias Génicas).
 * <p>
 * Modela el transporte de fármacos termosensibles aplicando cinéticas de Arrhenius
 * para predecir la pérdida de potencia y certificar la integridad del lote farmacéutico.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference EMA Good Distribution Practice (GDP); WHO Technical Report Series No. 961; Arrhenius Kinetic Model
 */
public record PharmaShipmentBatch(
        BatchId id,
        String tenantId,
        String drugName,
        DrugCategory category,
        ThermalEnvelope envelope,
        double currentPotencyLossPct,
        List<ThermalTelemetryReading> readings,
        BatchReleaseStatus releaseStatus,
        Instant dispatchedAt
) implements Serializable {

    public record BatchId(String value) {
        public BatchId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("BatchId no puede estar vacío");
        }
    }

    public enum DrugCategory {
        GLP1_PEPTIDE, MRNA_VACCINE, MONOCLONAL_ANTIBODY, CAR_T_CELL_THERAPY
    }

    public record ThermalEnvelope(
            double minTempCelsius,
            double maxTempCelsius,
            double maxAllowedPotencyLossPct,
            double activationEnergyKjMol // Ea para Arrhenius
    ) {}

    public record ThermalTelemetryReading(
            double temperatureCelsius,
            double humidityPct,
            long timestampEpochMs,
            boolean isTemperatureExcursion
    ) {}

    public enum BatchReleaseStatus {
        IN_TRANSIT_OPTIMAL, EXCURSION_EVALUATING, GDP_COMPLIANT_RELEASED, REJECTED_POTENCY_LOST
    }

    public PharmaShipmentBatch recordThermalReading(double tempCelsius, double humidityPct) {
        boolean excursion = tempCelsius < envelope.minTempCelsius() || tempCelsius > envelope.maxTempCelsius();
        ThermalTelemetryReading reading = new ThermalTelemetryReading(
                tempCelsius, humidityPct, System.currentTimeMillis(), excursion
        );

        List<ThermalTelemetryReading> history = new java.util.ArrayList<>(this.readings);
        history.add(reading);

        // Degradación cinética de Arrhenius simplificada
        double deltaPotency = 0.0;
        if (excursion) {
            double tempDiff = Math.abs(tempCelsius - envelope.maxTempCelsius());
            deltaPotency = 0.05 * Math.exp(0.1 * tempDiff); // Incremento exponencial
        }

        double nextLoss = this.currentPotencyLossPct + deltaPotency;

        BatchReleaseStatus nextStatus = BatchReleaseStatus.IN_TRANSIT_OPTIMAL;
        if (nextLoss >= envelope.maxAllowedPotencyLossPct()) {
            nextStatus = BatchReleaseStatus.REJECTED_POTENCY_LOST;
        } else if (excursion) {
            nextStatus = BatchReleaseStatus.EXCURSION_EVALUATING;
        }

        return new PharmaShipmentBatch(
                this.id,
                this.tenantId,
                this.drugName,
                this.category,
                this.envelope,
                nextLoss,
                List.copyOf(history),
                nextStatus,
                this.dispatchedAt
        );
    }
}
