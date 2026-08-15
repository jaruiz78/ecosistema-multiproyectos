package com.corp.ecosystem.ecotasa.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: RegionalEcoTaxSettlement (Ecotasa Turística Autonómica y Fondo de Sostenibilidad).
 */
public record RegionalEcoTaxSettlement(
        SettlementId id,
        String autonomousCommunityId,
        String accommodationEstId,
        EcoTaxDetails details,
        SettlementLedgerStatus status,
        Instant settledAt
) implements Serializable {

    public record SettlementId(String value) {
        public SettlementId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("SettlementId no puede estar vacío");
        }
    }

    public record EcoTaxDetails(
            int guestNightsCount,
            double ratePerNightEur,
            double totalCollectedEur,
            String dedicatedEnvironmentalProject,
            String zkAuditHash
    ) {}

    public enum SettlementLedgerStatus {
        SETTLED_AUDITED_ZK, PENDING_DISBURSEMENT_RESTORATION, AUDIT_FLAG_DISCREPANCY
    }

    public static RegionalEcoTaxSettlement createSettlement(
            SettlementId id,
            String commId,
            String estId,
            int guestNights,
            double rate,
            String project,
            String zkProof
    ) {
        double total = guestNights * rate;
        EcoTaxDetails details = new EcoTaxDetails(guestNights, rate, total, project, zkProof);
        return new RegionalEcoTaxSettlement(id, commId, estId, details, SettlementLedgerStatus.SETTLED_AUDITED_ZK, Instant.now());
    }
}
