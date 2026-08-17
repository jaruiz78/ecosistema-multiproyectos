package com.corp.core.math.thermo;

import java.io.Serializable;

/**
 * Calculador de destrucción de exergía basado en el Teorema de Gouy-Stodola:
 * \[
 * \dot{E}x_{\text{dest}} = T_0 \dot{S}_{\text{gen}} = T_0 \left( \dot{m} (s_{\text{out}} - s_{\text{in}}) - \sum \frac{\dot{Q}_k}{T_k} \right)
 * \]
 */
public record ExergyDestructionCalculator() implements Serializable {

    public static double computeExergyDestructionKw(
            double ambientTempKelvin,
            double massFlowKgS,
            double specificEntropyInKjKgK,
            double specificEntropyOutKjKgK,
            double heatTransferKw,
            double boundaryTempKelvin
    ) {
        double entropyGenerationKwK = massFlowKgS * (specificEntropyOutKjKgK - specificEntropyInKjKgK) - (heatTransferKw / boundaryTempKelvin);
        double exergyDestruction = ambientTempKelvin * Math.max(0.0, entropyGenerationKwK);
        return exergyDestruction;
    }

    public static double computePhysicalExergyKw(
            double massFlowKgS,
            double enthalpyInKjKg,
            double enthalpyAmbientKjKg,
            double entropyInKjKgK,
            double entropyAmbientKjKgK,
            double ambientTempKelvin
    ) {
        // e = (h - h0) - T0 * (s - s0)
        double specificExergy = (enthalpyInKjKg - enthalpyAmbientKjKg) - ambientTempKelvin * (entropyInKjKgK - entropyAmbientKjKgK);
        return massFlowKgS * Math.max(0.0, specificExergy);
    }
}
