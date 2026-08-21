package com.corp.coremps.application;

import com.corp.core.math.mps.MatrixProductState;
import com.corp.core.math.mps.TensorTrainCompressor;
import com.corp.coremps.domain.MpsStateCompressionReport;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QuantumTensorCompressionUseCase {

    public MpsStateCompressionReport compressQuantumGridState(String stateId, int numQubits, int bondDimensionChi) {
        MatrixProductState mps = TensorTrainCompressor.compressUniformState(numQubits, 2, bondDimensionChi);

        double uncompressed = Math.pow(2.0, numQubits);
        double compressed = numQubits * 2.0 * bondDimensionChi * bondDimensionChi;
        double ratio = Math.max(0.0, (1.0 - (compressed / uncompressed)) * 100.0);

        return new MpsStateCompressionReport(
                stateId,
                numQubits,
                bondDimensionChi,
                uncompressed,
                compressed,
                ratio,
                mps.calculateNorm() > 0.0
        );
    }
}
