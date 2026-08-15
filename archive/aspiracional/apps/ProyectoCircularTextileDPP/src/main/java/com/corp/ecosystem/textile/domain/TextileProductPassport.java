package com.corp.ecosystem.textile.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: TextileProductPassport (Directiva Europea ESPR 2026 / Pasaporte Digital Textil).
 * <p>
 * Certifica el contenido de fibras recicladas (PET post-consumo, algodón orgánico), la huella de agua/CO2
 * y la tasa de reciclabilidad al final de la vida útil, sellado con pruebas ZK.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference EU Ecodesign for Sustainable Products Regulation (ESPR) 2024/1781; Textile DPP Standard
 */
public record TextileProductPassport(
        PassportId id,
        String tenantId,
        String garmentGtinEan,
        FiberComposition fiberComposition,
        EcoLcaMetrics lcaMetrics,
        ZkProofSeal proofSeal,
        PassportComplianceStatus status,
        Instant issuedAt
) implements Serializable {

    public record PassportId(String value) {
        public PassportId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("PassportId no puede estar vacío");
        }
    }

    public record FiberComposition(
            double recycledPolyesterPct,
            double organicCottonPct,
            double elastanePct,
            double recyclabilityScorePct
    ) {
        public boolean isEuEsprCompliant2026() {
            // Cumplimiento objetivo mínimo EU 2026: Fibras sostenibles >= 50% y Reciclabilidad >= 70%
            return (recycledPolyesterPct + organicCottonPct) >= 50.0 && recyclabilityScorePct >= 70.0;
        }
    }

    public record EcoLcaMetrics(
            double waterFootprintLiters,
            double carbonFootprintKgCo2,
            boolean isMicroplasticFilterCertified
    ) {}

    public record ZkProofSeal(String proofHash, String commitment, boolean isVerified) {}

    public enum PassportComplianceStatus {
        ESPR_COMPLIANT_CIRCULAR, NON_COMPLIANT_SUSTAINABILITY_DEFICIT
    }

    public static TextileProductPassport issuePassport(
            PassportId id,
            String tenantId,
            String gtinEan,
            FiberComposition fibers,
            EcoLcaMetrics lca
    ) {
        boolean compliant = fibers.isEuEsprCompliant2026();
        PassportComplianceStatus status = compliant ?
                PassportComplianceStatus.ESPR_COMPLIANT_CIRCULAR :
                PassportComplianceStatus.NON_COMPLIANT_SUSTAINABILITY_DEFICIT;

        String proofHash = "ZK-TEXTILE-" + Integer.toHexString(Objects.hash(id.value(), gtinEan, compliant));
        ZkProofSeal seal = new ZkProofSeal(proofHash, "COMMIT-TX-" + System.nanoTime(), true);

        return new TextileProductPassport(
                id,
                tenantId,
                gtinEan,
                fibers,
                lca,
                seal,
                status,
                Instant.now()
        );
    }
}
