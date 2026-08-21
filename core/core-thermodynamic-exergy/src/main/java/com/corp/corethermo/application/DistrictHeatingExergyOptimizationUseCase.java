package com.corp.corethermo.application;

import com.corp.core.math.thermo.CarnotSecondLawEfficiency;
import com.corp.core.math.thermo.ExergyDestructionCalculator;
import com.corp.corethermo.domain.ExergyAnalysisReport;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DistrictHeatingExergyOptimizationUseCase {

    public ExergyAnalysisReport auditDistrictHeatingLoop(
            String plantId,
            double massFlowKgS,
            double tSupplyKelvin,
            double tReturnKelvin,
            double tAmbientKelvin
    ) {
        // Entalpías y entropías aproximadas para agua líquida
        double cp = 4.184; // kJ/kgK
        double hSupply = cp * tSupplyKelvin;
        double hReturn = cp * tReturnKelvin;
        double hAmbient = cp * tAmbientKelvin;

        double sSupply = cp * Math.log(tSupplyKelvin);
        double sReturn = cp * Math.log(tReturnKelvin);
        double sAmbient = cp * Math.log(tAmbientKelvin);

        double exergyIn = ExergyDestructionCalculator.computePhysicalExergyKw(massFlowKgS, hSupply, hAmbient, sSupply, sAmbient, tAmbientKelvin);
        double exergyOut = ExergyDestructionCalculator.computePhysicalExergyKw(massFlowKgS, hReturn, hAmbient, sReturn, sAmbient, tAmbientKelvin);

        double destroyed = Math.max(0.0, exergyIn - exergyOut);
        double eff = CarnotSecondLawEfficiency.evaluateSecondLawEfficiency(exergyOut, exergyIn) * 100.0;

        return new ExergyAnalysisReport(plantId, exergyIn, destroyed, eff, eff > 65.0);
    }
}
