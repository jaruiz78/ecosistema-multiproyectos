package com.corp.govtech.ledger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

/**
 * Servicio centralizado de registro inmutable (Audit-Trail Ledger) Zero-Trust.
 * Utilizado por ProyectoB2G, ProyectoTokenRWA y ProyectoCircular.
 */
public final class AuditLedgerService {

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

    private static String computeSha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
