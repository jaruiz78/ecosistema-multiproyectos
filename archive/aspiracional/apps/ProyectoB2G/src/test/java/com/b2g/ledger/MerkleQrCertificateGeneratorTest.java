package com.b2g.ledger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MerkleQrCertificateGenerator - Tests de Certificados de Auditoría Pública B2G")
class MerkleQrCertificateGeneratorTest {

    private final MerkleQrCertificateGenerator generator = new MerkleQrCertificateGenerator();

    @Test
    @DisplayName("Debe generar certificado Merkle con firma Cosign y código QR")
    void testGenerateCertificate() {
        String recordId = "PUB-LICITACION-2026-99";
        String txHash = "0x89ab12cd34ef5678";

        MerkleQrCertificateGenerator.MerkleQrCertificate cert = generator.generateCertificate(recordId, txHash);

        assertNotNull(cert);
        assertEquals(recordId, cert.auditRecordId());
        assertTrue(cert.merkleRootHash().startsWith("0xMERKLE"));
        assertTrue(cert.cosignSignature().contains("SIGSTORE_COSIGN_SLSA_L4"));
        assertTrue(cert.qrCodeDataUrl().startsWith("data:image/svg+xml;base64,"));
        assertNotNull(cert.generatedAt());
    }

    @Test
    @DisplayName("Debe validar no-nulidad en el record de certificado")
    void testCertificateNullValidation() {
        assertThrows(NullPointerException.class, () -> new MerkleQrCertificateGenerator.MerkleQrCertificate(
                null, "0x123", "sig", "data", java.time.Instant.now()
        ));
    }
}
