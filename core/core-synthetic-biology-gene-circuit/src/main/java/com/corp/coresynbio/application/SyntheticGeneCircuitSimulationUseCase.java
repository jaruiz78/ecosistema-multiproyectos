package com.corp.coresynbio.application;

import com.corp.core.math.synbio.GeneticLogicGateModel;
import com.corp.coresynbio.domain.GeneExpressionProfile;

public class SyntheticGeneCircuitSimulationUseCase {

    public GeneExpressionProfile simulateBiosensorGate(String circuitId, String hostChassis, double inducerA, double inducerB) {
        GeneticLogicGateModel andGate = new GeneticLogicGateModel(circuitId, GeneticLogicGateModel.GateType.AND_GATE, 0.05, 10.0);
        double outputRpu = andGate.evaluateGateOutput(inducerA, inducerB);
        boolean stateHigh = outputRpu > 5.0;

        return new GeneExpressionProfile(circuitId, hostChassis, inducerA, inducerB, outputRpu, stateHigh);
    }
}
