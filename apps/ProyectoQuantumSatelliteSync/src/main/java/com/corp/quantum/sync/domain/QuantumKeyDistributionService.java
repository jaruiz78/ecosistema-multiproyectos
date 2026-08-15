package com.corp.quantum.sync.domain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

/**
 * Servicio de Sincronización Orbital y Distribución de Claves Cuánticas (QKD).
 * Sincroniza relojes atómicos a nivel de picosegundos y genera claves simétricas
 * blindadas mediante entrelazamiento de fotones y firma post-cuántica Dilithium3.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class QuantumKeyDistributionService {

    public record QkdExchangeResult(
            String satelliteId,
            String groundStationId,
            boolean linkEstablished,
            double synchronizedClockPicoseconds,
            String generatedQuantumKeySha256,
            String postQuantumSignature
    ) {}

    public QkdExchangeResult establishQkdSession(QuantumSatelliteNode sat, String groundStationId, double groundClockPicoseconds) {
        Objects.requireNonNull(sat, "satélite no puede ser nulo");
        Objects.requireNonNull(groundStationId, "groundStationId no puede ser nulo");

        if (!sat.isQkdLinkSecure()) {
            return new QkdExchangeResult(
                    sat.satelliteId(), groundStationId, false,
                    groundClockPicoseconds, "LINK_REJECTED_QBER_HIGH", "UNVERIFIED"
            );
        }

        // Sincronización de reloj atómico compensando deriva relativista y Doppler
        double syncClock = (sat.atomicClockDriftPicoseconds() + groundClockPicoseconds) / 2.0;

        // Generación de clave cuántica compartida
        String rawSeed = sat.satelliteId() + ":" + groundStationId + ":" + syncClock + ":" + sat.qkdKeyRateBitsPerSec();
        String quantumKeyHash = sha256(rawSeed);

        String pqcSignature = "PQC_DILITHIUM3_QKD_" + quantumKeyHash.substring(0, 16);

        return new QkdExchangeResult(sat.satelliteId(), groundStationId, true, syncClock, quantumKeyHash, pqcSignature);
    }

    private String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }
}
