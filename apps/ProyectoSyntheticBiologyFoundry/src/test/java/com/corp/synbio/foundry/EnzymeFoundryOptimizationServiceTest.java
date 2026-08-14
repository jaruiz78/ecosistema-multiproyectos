package com.corp.synbio.foundry;

import com.corp.synbio.foundry.domain.EnzymeFoundryOptimizationService;
import com.corp.synbio.foundry.domain.SyntheticEnzymeVariant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EnzymeFoundryOptimizationServiceTest {

    private EnzymeFoundryOptimizationService foundryService;

    @BeforeEach
    void setUp() {
        foundryService = new EnzymeFoundryOptimizationService();
    }

    @Test
    void testCertifyBioreactorRun() {
        var v1 = new SyntheticEnzymeVariant("RUBISCO_MUT_v3", "RuBisCO", "a1b2c3d4e5", 145.0, 52.0, 8.5);
        var v2 = new SyntheticEnzymeVariant("CARB_ANHYD_MUT_v1", "CarbonicAnhydrase", "f6g7h8i9j0", 210.0, 48.0, 12.0);
        var vNonViable = new SyntheticEnzymeVariant("RUBISCO_UNSTABLE", "RuBisCO", "k1l2m3n4", 50.0, 30.0, 2.0);

        var cert = foundryService.certifyBioreactorRun("BIOREACTOR_ALMERIA_01", List.of(v1, v2, vNonViable), 1000.0);

        assertNotNull(cert);
        assertEquals("BIOREACTOR_ALMERIA_01", cert.bioreactorId());
        assertEquals("CARB_ANHYD_MUT_v1", cert.optimalVariantId());
        assertEquals(2, cert.viableVariantsEvaluated());
        assertEquals(288.0, cert.totalCo2CapturedKg24h(), 0.1);
        assertEquals(64, cert.carbonCreditCertificateSha256().length());
        assertTrue(cert.zkMerkleProofHash().startsWith("ZK_SNARK_CARBON_BIO_"));
    }
}
