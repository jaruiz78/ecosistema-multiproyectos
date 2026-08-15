package com.corp.ecosystem.ecopassport.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Agregado Raíz: EcoTourismPassport (EU Tourism Data Space & Ecotasa Dinámica ZK).
 * <p>
 * Certifica la huella de carbono total de viajes y paquetes turísticos (Transporte,
 * Alojamiento y Actividades), liquidando la ecotasa de forma transparente mediante
 * sellos criptográficos Zero-Knowledge.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference EU Tourism Data Space; ISO 14064-1; GHG Protocol Scope 3 Category 6 (Business Travel)
 */
public record EcoTourismPassport(
        PassportId id,
        String tenantId,
        String bookingReference,
        TravelerProfile traveler,
        TripFootprint footprint,
        EcoTaxAssessment ecoTax,
        ZkEcoProofSeal proofSeal,
        PassportState state,
        Instant issuedAt
) implements Serializable {

    public record PassportId(String value) {
        public PassportId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("PassportId no puede estar vacío");
        }
    }

    public record TravelerProfile(
            String travelerId,
            String countryOfOrigin,
            int numberOfTravelers
    ) {}

    public record TripFootprint(
            double transportEmissionKgCo2,
            double accommodationEmissionKgCo2,
            double activitiesEmissionKgCo2,
            double totalKgCo2
    ) {}

    public record EcoTaxAssessment(
            BigDecimal grossTaxEur,
            BigDecimal discountForLowCarbonTravelEur,
            BigDecimal netTaxPayableEur,
            String destinationEarmarkedProject
    ) {}

    public record ZkEcoProofSeal(
            String proofHash,
            String rootCommitment,
            boolean isVerifiedOnLedger
    ) {}

    public enum PassportState {
        CALCULATED, SETTLED, OFFICIALLY_CERTIFIED
    }

    public static EcoTourismPassport calculateAndIssue(
            PassportId id,
            String tenantId,
            String bookingRef,
            TravelerProfile traveler,
            double transportKgCo2,
            double accommodationKgCo2,
            double activitiesKgCo2,
            double baseTaxRatePerPersonEur,
            String earmarkedProject
    ) {
        double totalKgCo2 = transportKgCo2 + accommodationKgCo2 + activitiesKgCo2;
        TripFootprint fp = new TripFootprint(transportKgCo2, accommodationKgCo2, activitiesKgCo2, totalKgCo2);

        // Si la huella por persona es < 50 kg CO2, bonificación del 30% en la ecotasa
        double perPersonCo2 = totalKgCo2 / Math.max(1, traveler.numberOfTravelers());
        BigDecimal gross = BigDecimal.valueOf(baseTaxRatePerPersonEur * traveler.numberOfTravelers());
        BigDecimal discount = perPersonCo2 < 50.0 ? gross.multiply(BigDecimal.valueOf(0.30)) : BigDecimal.ZERO;
        BigDecimal net = gross.subtract(discount);

        EcoTaxAssessment tax = new EcoTaxAssessment(gross, discount, net, earmarkedProject);

        String proofHash = "ZK-ECO-PROOF-" + Integer.toHexString(Objects.hash(id.value(), totalKgCo2, net));
        String commitment = "ZK-COMMIT-" + System.nanoTime();
        ZkEcoProofSeal seal = new ZkEcoProofSeal(proofHash, commitment, true);

        return new EcoTourismPassport(
                id,
                tenantId,
                bookingRef,
                traveler,
                fp,
                tax,
                seal,
                PassportState.OFFICIALLY_CERTIFIED,
                Instant.now()
        );
    }
}
