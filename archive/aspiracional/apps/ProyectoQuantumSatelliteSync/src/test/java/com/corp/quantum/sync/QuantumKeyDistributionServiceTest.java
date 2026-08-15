package com.corp.quantum.sync;

import com.corp.quantum.sync.domain.QuantumKeyDistributionService;
import com.corp.quantum.sync.domain.QuantumSatelliteNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantumKeyDistributionServiceTest {

    private QuantumKeyDistributionService qkdService;

    @BeforeEach
    void setUp() {
        qkdService = new QuantumKeyDistributionService();
    }

    @Test
    void testSuccessfulQkdExchange() {
        var sat = new QuantumSatelliteNode("LEO_SAT_QKD_01", 550.0, 27500.0, 1.25, 100000.0, 0.035);
        var result = qkdService.establishQkdSession(sat, "GROUND_BASE_MADRID", 1.20);

        assertNotNull(result);
        assertTrue(result.linkEstablished());
        assertEquals("LEO_SAT_QKD_01", result.satelliteId());
        assertEquals("GROUND_BASE_MADRID", result.groundStationId());
        assertEquals(1.225, result.synchronizedClockPicoseconds(), 0.001);
        assertEquals(64, result.generatedQuantumKeySha256().length());
        assertTrue(result.postQuantumSignature().startsWith("PQC_DILITHIUM3_QKD_"));
    }

    @Test
    void testRejectedQkdLinkDueToHighQber() {
        var compromisedSat = new QuantumSatelliteNode("LEO_SAT_EAVESDROPPED", 600.0, 27000.0, 2.0, 50000.0, 0.18);
        var result = qkdService.establishQkdSession(compromisedSat, "GROUND_BASE_LONDON", 2.0);

        assertNotNull(result);
        assertFalse(result.linkEstablished(), "Enlace con QBER > 11% debe ser rechazado inmediatamente");
        assertEquals("LINK_REJECTED_QBER_HIGH", result.generatedQuantumKeySha256());
    }
}
