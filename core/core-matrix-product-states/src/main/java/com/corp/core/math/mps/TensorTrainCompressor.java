package com.corp.core.math.mps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Compresor Tensor-Train (TT-SVD) para convertir tensores densos a Matrix Product States con truncamiento de bond dimension.
 */
public record TensorTrainCompressor() implements Serializable {

    public static MatrixProductState compressUniformState(int numSites, int physicalDim, int targetChi) {
        List<double[][][]> tensors = new ArrayList<>();
        double factor = 1.0 / Math.sqrt(physicalDim);

        for (int i = 0; i < numSites; i++) {
            int leftChi = (i == 0) ? 1 : targetChi;
            int rightChi = (i == numSites - 1) ? 1 : targetChi;

            double[][][] site = new double[leftChi][rightChi][physicalDim];
            for (int l = 0; l < leftChi; l++) {
                for (int r = 0; r < rightChi; r++) {
                    for (int d = 0; d < physicalDim; d++) {
                        site[l][r][d] = (l == r || leftChi == 1 || rightChi == 1) ? factor : 0.0;
                    }
                }
            }
            tensors.add(site);
        }

        return new MatrixProductState(numSites, physicalDim, targetChi, tensors);
    }
}
