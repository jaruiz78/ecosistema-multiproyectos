package com.corp.govtech.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

/**
 * Servicio centralizado de registro inmutable (Audit-Trail Ledger) Zero-Trust.
 * Utilizado por ProyectoB2G, ProyectoTokenRWA y ProyectoCircular.
 * Optimizado con HexFormat nativo de Java 25.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_0_sistemas_distribuidos/01_modelos_de_sistemas_distribuidos.md">Documentación y Módulo Formativo</a>
 * @reference Lamport (1978) Time, Clocks, and the Ordering of Events in a Distributed System
 
 */
public final class AuditLedgerService {

    private static final HexFormat HEX_FORMAT = HexFormat.of();

    private AuditLedgerService() {}

    public record AuditRecord(
            String entityId,
            String eventType,
            String payloadHash,
            String previousHash,
            long timestampEpochMs
    ) {}

    /**
     * Genera un bloque audit-trail inmutable hash SHA-256 en tiempo O(1).
     */
    public static AuditRecord createAuditBlock(String entityId, String eventType, String payloadData, String previousHash) {
        long now = Instant.now().toEpochMilli();
        String rawContent = entityId + ":" + eventType + ":" + payloadData + ":" + previousHash + ":" + now;
        String hash = computeSha256(rawContent);
        return new AuditRecord(entityId, eventType, hash, previousHash, now);
    }

    public static String computeSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HEX_FORMAT.formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible en el runtime JVM", e);
        }
    }
}
