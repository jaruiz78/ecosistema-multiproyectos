package com.corp.core.math.hyperspectral;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NFindrEndmemberExtractorTest {

    @Test
    @DisplayName("Debe extraer miembros puros y desmezclar firmas espectrales con suma de abundancias = 1.0")
    void testSpectralUnmixing() {
        double[][] library = new double[][]{
                {0.8, 0.2, 0.1},
                {0.1, 0.9, 0.3}
        };

        var endmembers = NFindrEndmemberExtractor.extractEndmembers(library, 2);
        assertEquals(2, endmembers.size());

        double[] mixed = new double[]{0.45, 0.55, 0.2};
        double[] abundances = LinearSpectralUnmixer.estimateAbundances(mixed, library);

        assertEquals(2, abundances.length);
        double sum = abundances[0] + abundances[1];
        assertEquals(1.0, sum, 1e-4);
    }
}
