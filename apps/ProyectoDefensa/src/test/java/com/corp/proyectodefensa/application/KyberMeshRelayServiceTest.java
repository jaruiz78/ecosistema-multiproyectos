package com.corp.proyectodefensa.application;

import com.corp.proyectodefensa.domain.model.KyberSecurityEnvelope;
import com.corp.proyectodefensa.domain.model.TacticalSensorNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite TDD Zero-Mockito para {@link KyberMeshRelayService}.
 */
class KyberMeshRelayServiceTest {

    private final KyberMeshRelayService service = new KyberMeshRelayService();

    @Test
    @DisplayName("Debe enrutar sobres tácticos Kyber e incrementar el conteo de saltos")
    void shouldRelayTacticalEnvelopeCorrectly() {
        TacticalSensorNode nodeA = new TacticalSensorNode("NODE_ALPHA", "AIR_DEFENSE", -45.0, true, Instant.now());
        TacticalSensorNode nodeB = new TacticalSensorNode("NODE_BRAVO", "SONAR_BUOY", -52.0, true, Instant.now());

        service.registerNode(nodeA);
        service.registerNode(nodeB);

        KyberSecurityEnvelope envelope = new KyberSecurityEnvelope(
                "ENV_KYBER_001",
                "TENANT_MIL_01",
                "NODE_ALPHA",
                "NODE_BRAVO",
                "b64_ciphertext_kyber1024_simulated_payload_data",
                "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                0,
                Instant.now()
        );

        KyberMeshRelayService.RelayResult result = service.relayEnvelope(envelope);

        assertTrue(result.accepted());
        assertEquals("ENV_KYBER_001", result.envelopeId());
        assertEquals("NODE_BRAVO", result.nextHopNodeId());
        assertEquals(1, result.updatedHopCount());
        assertEquals("RELAY_DISPATCHED", result.routingStatus());
    }

    @Test
    @DisplayName("Debe detectar y rechazar ataques de repetición (Anti-Replay) en O(1)")
    void shouldDetectAndRejectReplayAttack() {
        KyberSecurityEnvelope envelope = new KyberSecurityEnvelope(
                "ENV_REPLAY_002",
                "TENANT_MIL_01",
                "NODE_ALPHA",
                "NODE_BRAVO",
                "b64_ciphertext",
                "digest123",
                1,
                Instant.now()
        );

        // Primer envío: aceptado
        KyberMeshRelayService.RelayResult first = service.relayEnvelope(envelope);
        assertTrue(first.accepted());

        // Segundo envío idéntico: rechazado por Anti-Replay
        KyberMeshRelayService.RelayResult second = service.relayEnvelope(envelope);
        assertFalse(second.accepted());
        assertEquals("DROP_REPLAY_ATTACK_DETECTED", second.routingStatus());
    }

    @Test
    @DisplayName("Debe descartar paquetes que superen el límite máximo de saltos (TTL)")
    void shouldDropPacketsExceedingMaxHops() {
        KyberSecurityEnvelope expiredEnvelope = new KyberSecurityEnvelope(
                "ENV_EXPIRED_003",
                "TENANT_MIL_01",
                "NODE_ALPHA",
                "NODE_BRAVO",
                "b64_ciphertext",
                "digest123",
                16, // Límite máximo es 16
                Instant.now()
        );

        KyberMeshRelayService.RelayResult result = service.relayEnvelope(expiredEnvelope);
        assertFalse(result.accepted());
        assertEquals("DROP_MAX_HOPS_EXCEEDED", result.routingStatus());
    }
}
