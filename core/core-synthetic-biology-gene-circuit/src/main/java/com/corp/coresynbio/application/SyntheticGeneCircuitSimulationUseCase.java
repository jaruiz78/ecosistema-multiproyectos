package com.corp.coresynbio.application;

import com.corp.core.math.synbio.GeneticLogicGateModel;
import com.corp.coresynbio.domain.GeneExpressionProfile;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SyntheticGeneCircuitSimulationUseCase {

    public GeneExpressionProfile simulateBiosensorGate(String circuitId, String hostChassis, double inducerA, double inducerB) {
        GeneticLogicGateModel andGate = new GeneticLogicGateModel(circuitId, GeneticLogicGateModel.GateType.AND_GATE, 0.05, 10.0);
        double outputRpu = andGate.evaluateGateOutput(inducerA, inducerB);
        boolean stateHigh = outputRpu > 5.0;

        return new GeneExpressionProfile(circuitId, hostChassis, inducerA, inducerB, outputRpu, stateHigh);
    }
}
