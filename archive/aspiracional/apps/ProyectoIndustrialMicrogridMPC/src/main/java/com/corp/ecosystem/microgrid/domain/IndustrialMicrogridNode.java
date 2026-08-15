package com.corp.ecosystem.microgrid.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: IndustrialMicrogridNode (Microredes Industriales y Flexibilidad de Demanda con MPC).
 * <p>
 * Optimiza en submilisegundos el despacho de hornos de arco/inducción, baterías BESS y cogeneración
 * para participar en mercados de balance de red secundaria (aFRR/mFRR) y peak shaving.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference IEEE 2030.7 Standard for the Specification of Microgrid Controllers; ENTSO-E Demand Response
 */
public record IndustrialMicrogridNode(
        NodeId id,
        String tenantId,
        String industrialParkName,
        AssetCapacities capacities,
        PowerFlowState currentState,
        MpcDispatchDecision lastDecision,
        Instant lastDispatchedAt
) implements Serializable {

    public record NodeId(String value) {
        public NodeId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("NodeId no puede estar vacío");
        }
    }

    public record AssetCapacities(
            double maxImportGridKw,
            double bessCapacityKwh,
            double bessMaxChargeDischargeKw,
            double curtailableLoadMaxKw
    ) {}

    public record PowerFlowState(
            double activeLoadKw,
            double bessSocPct,
            double gridFrequencyHz,
            double currentTariffEurPerKwh
    ) {}

    public record MpcDispatchDecision(
            double gridImportKw,
            double bessDischargeKw,
            double loadCurtailedKw,
            double estimatedCostSavingsEur,
            boolean isFrequencySupportActive
    ) {}

    public IndustrialMicrogridNode dispatchMpc(double loadKw, double freqHz, double tariffEur) {
        boolean freqSupport = (freqHz < 49.85); // Caída de frecuencia en red
        double bessDischarge = 0.0;
        double loadCurtail = 0.0;

        if (freqSupport || tariffEur > 0.25) {
            // Descarga BESS para soporte de frecuencia o precio pico
            bessDischarge = Math.min(capacities.bessMaxChargeDischargeKw(), loadKw);
            if (freqSupport && (loadKw - bessDischarge) > capacities.maxImportGridKw()) {
                loadCurtail = Math.min(capacities.curtailableLoadMaxKw(), loadKw - bessDischarge - capacities.maxImportGridKw());
            }
        }

        double gridImport = Math.max(0.0, loadKw - bessDischarge - loadCurtail);
        double savings = (bessDischarge + loadCurtail) * tariffEur;

        PowerFlowState nextState = new PowerFlowState(loadKw, currentState.bessSocPct(), freqHz, tariffEur);
        MpcDispatchDecision decision = new MpcDispatchDecision(gridImport, bessDischarge, loadCurtail, savings, freqSupport);

        return new IndustrialMicrogridNode(
                this.id,
                this.tenantId,
                this.industrialParkName,
                this.capacities,
                nextState,
                decision,
                Instant.now()
        );
    }
}
