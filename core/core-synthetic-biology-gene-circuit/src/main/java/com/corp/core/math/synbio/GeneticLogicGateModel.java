package com.corp.core.math.synbio;

import java.io.Serializable;

/**
 * Puerta lógica biológica sintética (AND, OR, NOT, NAND, NOR) implementada sobre promotores celulares.
 */
public record GeneticLogicGateModel(
        String gateId,
        GateType type,
        double baselineExpression,
        double maxExpression
) implements Serializable {

    public enum GateType {
        AND_GATE,
        OR_GATE,
        NOT_INVERTER
    }

    public double evaluateGateOutput(double inputA, double inputB) {
        return switch (type) {
            case AND_GATE -> {
                double rA = HillKineticsGeneSolver.computeActivationRate(inputA, 1.0, 5.0, 2.0);
                double rB = HillKineticsGeneSolver.computeActivationRate(inputB, 1.0, 5.0, 2.0);
                yield baselineExpression + (maxExpression - baselineExpression) * (rA * rB);
            }
            case OR_GATE -> {
                double rA = HillKineticsGeneSolver.computeActivationRate(inputA, 1.0, 5.0, 2.0);
                double rB = HillKineticsGeneSolver.computeActivationRate(inputB, 1.0, 5.0, 2.0);
                yield baselineExpression + (maxExpression - baselineExpression) * Math.max(rA, rB);
            }
            case NOT_INVERTER -> {
                double rA = HillKineticsGeneSolver.computeRepressionRate(inputA, 1.0, 5.0, 2.0);
                yield baselineExpression + (maxExpression - baselineExpression) * rA;
            }
        };
    }
}
