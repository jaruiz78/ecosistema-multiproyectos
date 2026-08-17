package com.corp.coresdp.application;

import com.corp.coresdp.domain.SdpLyapunovCertificate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SdpIntegrationTest {

    @Test
    @DisplayName("Debe verificar estabilidad de sistema no lineal mediante caso de uso SDP")
    void testVerifyNonlinearSystemStability() {
        var useCase = new SemidefiniteProgrammingUseCase();
        double[][] gram = {
                {5.0, 0.5},
                {0.5, 2.0}
        };

        SdpLyapunovCertificate cert = useCase.verifyNonlinearSystemStability("ORBIT-CR3BP-L1", gram);
        assertNotNull(cert);
        assertEquals("ORBIT-CR3BP-L1", cert.systemIdentifier());
        assertTrue(cert.isPositiveSemidefinite());
    }
}
