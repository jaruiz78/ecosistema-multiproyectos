package com.corp.ecosystem.carbonledger.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: DigitalProductPassport (EU DPP 2026 / ISO 14040/14044).
 * <p>
 * Representa el pasaporte digital inmutable de un lote o producto industrial,
 * certificando la huella de carbono embebida, la tasa de reciclabilidad y la
 * prueba criptográfica Zero-Knowledge asociada.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-015-declarative-bigquery-reconciler-and-edge-outbox.md">ADR-015</a>
 * @reference ISO 14064 (Greenhouse Gases); EU Regulation 2024/1781 (Ecodesign for Sustainable Products)
 */
public record DigitalProductPassport(
        PassportId id,
        String tenantId,
        String batchIdentifier,
        ProductCategory category,
        CarbonFootprint footprint,
        CircularMetrics circularity,
        ZkProofSeal proofSeal,
        PassportState state,
        Instant issuedAt
) implements Serializable {

    public record PassportId(String value) {
        public PassportId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("PassportId no puede estar vacío");
        }
    }

    public enum ProductCategory {
        INDUSTRIAL_BATTERY, TEXTILE, AGRI_BIO_MATERIAL, CONSTRUCTION_STEEL, ELECTRONICS
    }

    public enum PassportState {
        DRAFT, CERTIFIED, ANCHORED_ON_LEDGER, REVOKED
    }

    public record CarbonFootprint(
            double rawMaterialEmissionKgCo2,
            double manufacturingEmissionKgCo2,
            double logisticsEmissionKgCo2,
            double co2AvoidedKg,
            double totalNetKgCo2PerUnit
    ) {
        public static CarbonFootprint compute(double raw, double mfg, double log, double avoided) {
            double total = Math.max(0.0, (raw + mfg + log) - avoided);
            return new CarbonFootprint(raw, mfg, log, avoided, total);
        }
    }

    public record CircularMetrics(
            double recycledContentPct,
            double recyclabilityRatePct,
            int expectedLifespanMonths
    ) {}

    public record ZkProofSeal(
            String merkleRootHash,
            String snarkProofHash,
            String verifierAuthority
    ) {}

    public DigitalProductPassport certify(ZkProofSeal seal) {
        if (this.state != PassportState.DRAFT) {
            throw new IllegalStateException("Solo los pasaportes en estado DRAFT pueden ser certificados");
        }
        return new DigitalProductPassport(
                this.id,
                this.tenantId,
                this.batchIdentifier,
                this.category,
                this.footprint,
                this.circularity,
                seal,
                PassportState.CERTIFIED,
                Instant.now()
        );
    }
}
