package com.corp.proyectofusionpowergrid.application.service;

import com.corp.core.math.thermo.ExergyDestructionCalculator;
import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import com.corp.proyectosmartgridstoragevpp.domain.model.BatteryStorageUnit;

import java.io.Serializable;

/**
 * Servicio de orquestación sinérgica que acopla el confinamiento magnético MHD del reactor Tokamak,
 * la optimización termodinámica de exergía (Gouy-Stodola) y el almacenamiento de energía en baterías BESS.
 */
public class FusionStorageExergyOrchestratorService implements Serializable {

    public record FusionGridDispatchResult(
            String reactorId,
            String batteryId,
            double generatedExergyMw,
            double destroyedExergyMw,
            double transferredToStorageKwh,
            boolean dispatchOptimal
    ) implements Serializable {}

    public FusionGridDispatchResult coordinateFusionAndStorageDispatch(
            TokamakPlasmaState plasmaState,
            BatteryStorageUnit bessUnit,
            double massFlowCoolantKgS,
            double tCoreCoolantKelvin,
            double tAmbientKelvin
    ) {
        // 1. Validar estabilidad MHD del reactor Tokamak
        boolean plasmaStable = plasmaState.quality() != TokamakPlasmaState.ConfinementQuality.DISRUPTION_WARNING;

        // 2. Calcular exergía física producida en el refrigerante primario (Helio gaseoso)
        double cp = 5.19; // Helio gaseoso kJ/kgK
        double hCore = cp * tCoreCoolantKelvin;
        double hAmb = cp * tAmbientKelvin;
        double sCore = cp * Math.log(tCoreCoolantKelvin);
        double sAmb = cp * Math.log(tAmbientKelvin);

        double exergyProducedKw = ExergyDestructionCalculator.computePhysicalExergyKw(
                massFlowCoolantKgS, hCore, hAmb, sCore, sAmb, tAmbientKelvin
        );
        double exergyProducedMw = exergyProducedKw / 1000.0;

        // 3. Estimar destrucción de exergía Gouy-Stodola en el intercambiador de calor
        double destroyedKw = ExergyDestructionCalculator.computeExergyDestructionKw(
                tAmbientKelvin, massFlowCoolantKgS, sAmb, sCore, 0.0, tCoreCoolantKelvin
        );
        double destroyedMw = Math.min(exergyProducedMw * 0.20, destroyedKw / 1000.0);

        // 4. Derivar excedente neto hacia la batería BESS
        double netThermalKw = Math.max(100.0, exergyProducedKw - (destroyedMw * 1000.0));
        double storedKwh = netThermalKw * 0.50; // Eficiencia ciclo Brayton helio ~50%
        bessUnit.charge(storedKwh, netThermalKw * 0.50);

        boolean optimal = plasmaStable && exergyProducedMw > 0.0;

        return new FusionGridDispatchResult(
                plasmaState.reactorId(),
                bessUnit.batteryId(),
                exergyProducedMw,
                destroyedMw,
                storedKwh,
                optimal
        );
    }
}
