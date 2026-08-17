package com.corp.proyectob2g.application;

import com.corp.proyectob2g.domain.model.PublicProcurementContract;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de Aplicación para el Registro Inmutable y Sellado de Licitaciones Públicas (GovTech Ledger).
 * Asegura la inmutabilidad de ofertas económicas y pliegos técnicos mediante encadenamiento SHA-256 en O(1).
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada Ecosistema</a>
 */
public final class PublicTenderAuditLedgerService {

    private static final HexFormat HEX = HexFormat.of();
    private final ReentrantLock lock = new ReentrantLock();
    private final Map<String, SealedTenderRecord> ledger = new ConcurrentHashMap<>();

    public record SealedTenderRecord(
            String tenderId,
            String governmentEntity,
            double budgetAmountEur,
            String payloadHash,
            String previousSealHash,
            String currentSealHash,
            long sealedTimestamp
    ) {}

    private String lastSealHash = "0000000000000000000000000000000000000000000000000000000000000000";

    /**
     * Sella criptográficamente una oferta de licitación pública en O(1).
     */
    public SealedTenderRecord sealTenderBid(PublicProcurementContract contract, String tenderDetailsJson) {
        Objects.requireNonNull(contract, "contract no puede ser nulo");
        Objects.requireNonNull(tenderDetailsJson, "tenderDetailsJson no puede ser nulo");

        lock.lock();
        try {
            String payloadHash = computeSha256(tenderDetailsJson);
            long timestamp = Instant.now().toEpochMilli();
            String prevHash = lastSealHash;
            String sealPayload = contract.contractId() + ":" + contract.governmentEntity() + ":" +
                    contract.budget() + ":" + payloadHash + ":" + prevHash + ":" + timestamp;
            String currentSealHash = computeSha256(sealPayload);

            SealedTenderRecord record = new SealedTenderRecord(
                    contract.contractId(),
                    contract.governmentEntity(),
                    contract.budget(),
                    payloadHash,
                    prevHash,
                    currentSealHash,
                    timestamp
            );

            ledger.put(contract.contractId(), record);
            lastSealHash = currentSealHash;
            return record;
        } finally {
            lock.unlock();
        }
    }

    public Optional<SealedTenderRecord> getSealedTender(String contractId) {
        return Optional.ofNullable(ledger.get(contractId));
    }

    private static String computeSha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return HEX.formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
