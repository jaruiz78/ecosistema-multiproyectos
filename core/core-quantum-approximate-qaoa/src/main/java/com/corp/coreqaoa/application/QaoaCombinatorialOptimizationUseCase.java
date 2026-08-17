package com.corp.coreqaoa.application;

import com.corp.core.math.qaoa.IsingSpinHamiltonian;
import com.corp.core.math.qaoa.QaoaCircuitSimulator;
import com.corp.coreqaoa.domain.QaoaOptimizationResult;

public class QaoaCombinatorialOptimizationUseCase {

    public QaoaOptimizationResult solveMaxCutGraph(String graphId, double[][] adjacencyMatrix) {
        IsingSpinHamiltonian hamiltonian = IsingSpinHamiltonian.maxCutGraph(adjacencyMatrix);
        int[] partition = QaoaCircuitSimulator.optimizeIsingConfiguration(hamiltonian, 0.45, 0.32);
        double groundEnergy = hamiltonian.evaluateStateEnergy(partition);

        return new QaoaOptimizationResult(graphId, adjacencyMatrix.length, partition, groundEnergy, true);
    }
}
