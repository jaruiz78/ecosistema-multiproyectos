package com.corp.coreqaoa.application;

import com.corp.core.math.qaoa.IsingSpinHamiltonian;
import com.corp.core.math.qaoa.QaoaCircuitSimulator;
import com.corp.coreqaoa.domain.QaoaOptimizationResult;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QaoaCombinatorialOptimizationUseCase {

    public QaoaOptimizationResult solveMaxCutGraph(String graphId, double[][] adjacencyMatrix) {
        IsingSpinHamiltonian hamiltonian = IsingSpinHamiltonian.maxCutGraph(adjacencyMatrix);
        int[] partition = QaoaCircuitSimulator.optimizeIsingConfiguration(hamiltonian, 0.45, 0.32);
        double groundEnergy = hamiltonian.evaluateStateEnergy(partition);

        return new QaoaOptimizationResult(graphId, adjacencyMatrix.length, partition, groundEnergy, true);
    }
}
