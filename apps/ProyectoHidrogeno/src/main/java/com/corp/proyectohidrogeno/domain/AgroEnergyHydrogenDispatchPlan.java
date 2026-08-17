package com.corp.proyectohidrogeno.domain;

import java.util.Objects;

/**
 * Plan de despacho óptimo integrado Agro-Voltaico e Hidrógeno Verde.
 *
 * @param planId                    Identificador único del plan
 * @param solarGenerationKw         Generación solar disponible en kW
 * @param allocatedElectrolyzerKw   Potencia despachada a electrolizadores PEM en kW
 * @param allocatedIrrigationKw     Potencia despachada a bombas de regadío en kW
 * @param expectedHydrogenKgPerHour Producción estimada de H2 en kg/h
 * @param waterConsumedLitersPerHour Consumo de agua en L/h (para electrólisis y riego)
 * @param formalProofDigest         Hash criptográfico del certificado de verificación formal
 * @param isVerified                Indica si el balance satisface todas las leyes de conservación
 *
 * @see docs/formacion_ecosistema/modulo_3_gemelo_digital_simulacion/10_gemelo_digital_unificado_core.md
 */
public record AgroEnergyHydrogenDispatchPlan(
        String planId,
        double solarGenerationKw,
        double allocatedElectrolyzerKw,
        double allocatedIrrigationKw,
        double expectedHydrogenKgPerHour,
        double waterConsumedLitersPerHour,
        String formalProofDigest,
        boolean isVerified
) {
    public AgroEnergyHydrogenDispatchPlan {
        Objects.requireNonNull(planId, "planId no puede ser nulo");
        Objects.requireNonNull(formalProofDigest, "formalProofDigest no puede ser nulo");
        if (solarGenerationKw < 0 || allocatedElectrolyzerKw < 0 || allocatedIrrigationKw < 0) {
            throw new IllegalArgumentException("Las potencias no pueden ser negativas");
        }
    }
}
