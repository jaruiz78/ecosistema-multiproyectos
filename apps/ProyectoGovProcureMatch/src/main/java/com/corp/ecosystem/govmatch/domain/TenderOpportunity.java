package com.corp.ecosystem.govmatch.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz: TenderOpportunity (Radar B2G & Scoring Semántico de Licitaciones).
 * <p>
 * Modela licitaciones del sector público, contrastando requisitos de solvencia técnica,
 * solvencia económica y criterios medioambientales frente al perfil del licitador.
 * </p>
 *
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Especificación de Verticales</a>
 * @reference Directiva 2014/24/UE sobre contratación pública; Ley 9/2017 de Contratos del Sector Público
 */
public record TenderOpportunity(
        OpportunityId id,
        String tenderRefCode,
        String contractingAuthority,
        BigDecimal budgetEur,
        List<String> cpvCodes,
        SolvencyCriteria requiredSolvency,
        OpportunityState state,
        Instant submissionDeadline
) implements Serializable {

    public record OpportunityId(String value) {
        public OpportunityId {
            Objects.requireNonNull(value, "value no puede ser nulo");
            if (value.isBlank()) throw new IllegalArgumentException("OpportunityId no puede estar vacío");
        }
    }

    public record SolvencyCriteria(
            BigDecimal minAnnualRevenueEur,
            int minYearsExperience,
            List<String> requiredIsoCertifications,
            double maxSubcontractingAllowedPct
    ) {}

    public record ContractorProfile(
            String contractorId,
            String legalName,
            BigDecimal annualRevenueEur,
            int yearsExperience,
            List<String> activeIsoCertifications,
            List<String> matchingCpvCodes
    ) {}

    public record EligibilityScore(
            boolean isFinanciallyEligible,
            boolean isTechnicallyEligible,
            double matchScorePct,
            List<String> missingRequirements
    ) {}

    public enum OpportunityState {
        PUBLISHED, EVALUATING, CLOSED, AWARDED
    }

    public EligibilityScore evaluateContractor(ContractorProfile contractor) {
        boolean financial = contractor.annualRevenueEur().compareTo(requiredSolvency.minAnnualRevenueEur()) >= 0;
        boolean technical = contractor.yearsExperience() >= requiredSolvency.minYearsExperience();

        List<String> missing = new java.util.ArrayList<>();
        if (!financial) missing.add("Facturación anual inferior a la exigida: " + requiredSolvency.minAnnualRevenueEur() + " EUR");
        if (!technical) missing.add("Experiencia acreditada menor a " + requiredSolvency.minYearsExperience() + " años");

        for (String iso : requiredSolvency.requiredIsoCertifications()) {
            if (!contractor.activeIsoCertifications().contains(iso)) {
                missing.add("Certificación ISO ausente: " + iso);
            }
        }

        double score = 100.0;
        if (!financial) score -= 40.0;
        if (!technical) score -= 30.0;
        score -= (missing.size() > 2 ? 20.0 : missing.size() * 10.0);
        score = Math.max(0.0, score);

        boolean fullyEligible = financial && technical && missing.isEmpty();
        return new EligibilityScore(financial, technical, score, List.copyOf(missing));
    }
}
