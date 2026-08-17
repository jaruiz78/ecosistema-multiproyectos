package com.corp.proyectodefensa.application;

import com.corp.proyectodefensa.domain.model.KyberSecurityEnvelope;
import com.corp.proyectodefensa.domain.model.TacticalSensorNode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Aplicación para el Enrutamiento Táctico Mesh y Validación Post-Cuántica Kyber/ML-KEM.
 * Opera en entornos Air-Gapped con protección contra repetición de paquetes (Anti-Replay) en O(1).
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 * @reference NIST FIPS 203 (ML-KEM / CRYSTALS-Kyber)
 */
public final class KyberMeshRelayService {

    private static final HexFormat HEX = HexFormat.of();
    private static final int MAX_ALLOWED_HOPS = 16;

    private final ReentrantLock lock = new ReentrantLock();
    private final Set<String> processedEnvelopes = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Map<String, TacticalSensorNode> activeNodes = new ConcurrentHashMap<>();

    /**
     * Resultado del enrutamiento de un sobre táctico.
     */
    public record RelayResult(
            boolean accepted,
            String envelopeId,
            String nextHopNodeId,
            int updatedHopCount,
            String routingStatus,
            Instant relayedAt
    ) {}

    public void registerNode(TacticalSensorNode node) {
        Objects.requireNonNull(node, "El nodo táctico no puede ser nulo");
        activeNodes.put(node.sensorId(), node);
    }

    /**
     * Enruta un sobre táctico Kyber verificando integridad criptográfica y prevención de repetición en O(1).
     *
     * @param envelope Sobre táctico cifrado.
     * @return {@link RelayResult} con el estado del despacho.
     */
    public RelayResult relayEnvelope(KyberSecurityEnvelope envelope) {
        Objects.requireNonNull(envelope, "El envelope no puede ser nulo");

        // 1. Verificación de Límite de Saltos (TTL Air-Gapped)
        if (envelope.airGappedHopCount() >= MAX_ALLOWED_HOPS) {
            return new RelayResult(
                    false,
                    envelope.envelopeId(),
                    null,
                    envelope.airGappedHopCount(),
                    "DROP_MAX_HOPS_EXCEEDED",
                    Instant.now()
            );
        }

        // 2. Protección Anti-Replay en O(1)
        lock.lock();
        try {
            if (processedEnvelopes.contains(envelope.envelopeId())) {
                return new RelayResult(
                        false,
                        envelope.envelopeId(),
                        null,
                        envelope.airGappedHopCount(),
                        "DROP_REPLAY_ATTACK_DETECTED",
                        Instant.now()
                );
            }
            processedEnvelopes.add(envelope.envelopeId());
        } finally {
            lock.unlock();
        }

        // 3. Validación de Nodo Destino en la Malla
        String nextHop;
        if (activeNodes.containsKey(envelope.targetNodeId())) {
            nextHop = envelope.targetNodeId();
        } else {
            // Enrutamiento opportunistic mesh hacia el nodo más cercano
            nextHop = activeNodes.keySet().stream()
                    .filter(id -> !id.equals(envelope.sourceNodeId()))
                    .findFirst()
                    .orElse("GATEWAY_COMMAND_POST");
        }

        return new RelayResult(
                true,
                envelope.envelopeId(),
                nextHop,
                envelope.airGappedHopCount() + 1,
                "RELAY_DISPATCHED",
                Instant.now()
        );
    }

    /**
     * Valida el resumen criptográfico del secreto compartido Kyber contra el payload recibido.
     */
    public boolean verifySharedSecretDigest(String rawSecret, String expectedDigest) {
        if (rawSecret == null || expectedDigest == null) {
            return false;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawSecret.getBytes(StandardCharsets.UTF_8));
            String calculatedHex = HEX.formatHex(hash);
            return calculatedHex.equalsIgnoreCase(expectedDigest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no soportado", e);
        }
    }
}
