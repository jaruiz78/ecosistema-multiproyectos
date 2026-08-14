package com.rwa.escrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("InstantZkEscrowVerifier - Tests de Verificación de Solvencia ZK-SNARK")
class InstantZkEscrowVerifierTest {

    private final InstantZkEscrowVerifier verifier = new InstantZkEscrowVerifier();

    @Test
    @DisplayName("Debe verificar solvencia exitosamente con prueba válida e importe positivo")
    void testVerifyEscrowSolvencyValid() {
        String tokenId = "RWA-AGRO-LAND-001";
        byte[] zkProof = new byte[]{1, 2, 3, 4, 5};
        double requiredMin = 50000.0;

        InstantZkEscrowVerifier.ZkVerificationResult result = verifier.verifyEscrowSolvency(tokenId, zkProof, requiredMin);

        assertNotNull(result);
        assertEquals(tokenId, result.assetTokenId());
        assertTrue(result.isSolvent());
        assertTrue(result.proofHash().startsWith("0xZK"));
        assertNotNull(result.verifiedAt());
    }

    @Test
    @DisplayName("Debe fallar si la prueba es nula o vacía o el importe es negativo")
    void testVerifyEscrowSolvencyInvalid() {
        InstantZkEscrowVerifier.ZkVerificationResult res1 = verifier.verifyEscrowSolvency("TOKEN-1", null, 100.0);
        assertFalse(res1.isSolvent());

        InstantZkEscrowVerifier.ZkVerificationResult res2 = verifier.verifyEscrowSolvency("TOKEN-1", new byte[0], 100.0);
        assertFalse(res2.isSolvent());

        InstantZkEscrowVerifier.ZkVerificationResult res3 = verifier.verifyEscrowSolvency("TOKEN-1", new byte[]{1}, -5.0);
        assertFalse(res3.isSolvent());
    }
}
