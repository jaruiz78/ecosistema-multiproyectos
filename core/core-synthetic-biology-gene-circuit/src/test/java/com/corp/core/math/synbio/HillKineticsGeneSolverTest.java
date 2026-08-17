package com.corp.core.math.synbio;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HillKineticsGeneSolverTest {

    @Test
    @DisplayName("Debe evaluar activación y represión genética según función sigmoidea de Hill")
    void testHillKinetics() {
        double actHigh = HillKineticsGeneSolver.computeActivationRate(10.0, 100.0, 5.0, 2.0);
        double actLow = HillKineticsGeneSolver.computeActivationRate(1.0, 100.0, 5.0, 2.0);

        assertTrue(actHigh > actLow);
        assertTrue(actHigh <= 100.0);

        double repHigh = HillKineticsGeneSolver.computeRepressionRate(10.0, 100.0, 5.0, 2.0);
        double repLow = HillKineticsGeneSolver.computeRepressionRate(1.0, 100.0, 5.0, 2.0);

        assertTrue(repHigh < repLow);
    }
}
