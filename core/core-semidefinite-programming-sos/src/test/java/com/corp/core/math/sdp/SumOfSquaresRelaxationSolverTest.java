package com.corp.core.math.sdp;

import com.corp.coresdp.domain.SdpLyapunovCertificate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SumOfSquaresRelaxationSolverTest {

    @Test
    @DisplayName("Debe verificar matriz de Gram semidefinida positiva y certificar estabilidad")
    void testGramMatrixPsdCertification() {
        double[][] qMatrix = {
                {4.0, 1.0},
                {1.0, 3.0}
        };

        assertTrue(SumOfSquaresRelaxationSolver.isGramMatrixPsd(qMatrix));

        SdpLyapunovCertificate cert = SumOfSquaresRelaxationSolver.certifyStability("VPP-GRID-STABILITY-01", qMatrix);
        assertNotNull(cert);
        assertTrue(cert.isPositiveSemidefinite());
        assertTrue(cert.stabilityMargin() > 0.0);
    }
}
