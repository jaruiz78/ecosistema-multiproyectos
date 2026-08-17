package com.corp.core.math.mps;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixProductStateTest {

    @Test
    @DisplayName("Debe comprimir estado uniforme con TensorTrainCompressor y calcular norma válida")
    void testMpsCompression() {
        var mps = TensorTrainCompressor.compressUniformState(10, 2, 4);

        assertEquals(10, mps.numSites());
        assertEquals(2, mps.physicalDimension());
        assertEquals(4, mps.bondDimensionChi());
        assertTrue(mps.calculateNorm() > 0.0);
    }
}
