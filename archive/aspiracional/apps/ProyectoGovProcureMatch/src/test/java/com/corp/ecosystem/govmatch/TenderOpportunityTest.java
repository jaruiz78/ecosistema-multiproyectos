package com.corp.ecosystem.govmatch;

import com.corp.ecosystem.govmatch.application.GovProcureMatchService;
import com.corp.ecosystem.govmatch.domain.TenderOpportunity;
import com.corp.ecosystem.govmatch.domain.port.TenderOpportunityRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias TDD Zero-Mockito para ProyectoGovProcureMatch.
 */
class TenderOpportunityTest {

    static class InMemoryTenderRepository implements TenderOpportunityRepositoryPort {
        private final Map<TenderOpportunity.OpportunityId, TenderOpportunity> storage = new ConcurrentHashMap<>();

        @Override
        public TenderOpportunity save(TenderOpportunity opportunity) {
            storage.put(opportunity.id(), opportunity);
            return opportunity;
        }

        @Override
        public Optional<TenderOpportunity> findById(TenderOpportunity.OpportunityId id) {
            return Optional.ofNullable(storage.get(id));
        }
    }

    private final InMemoryTenderRepository repository = new InMemoryTenderRepository();
    private final GovProcureMatchService service = new GovProcureMatchService(repository);

    @Test
    @DisplayName("Debe calificar con 100% de elegibilidad a un contratista que cumple solvencia económica, técnica e ISO")
    void shouldScore100PctWhenFullyEligible() {
        TenderOpportunity tender = service.publishTender(
                "LIC-2026-CLOUD-01",
                "Ministerio de Transformacion Digital",
                new BigDecimal("1500000.00"),
                List.of("72000000-5", "72200000-7"),
                new BigDecimal("800000.00"), // 800k revenue requerido
                3,                            // 3 años experiencia
                List.of("ISO-9001", "ISO-27001"),
                Instant.now().plusSeconds(86400 * 30)
        );

        TenderOpportunity.ContractorProfile contractor = new TenderOpportunity.ContractorProfile(
                "corp-tech-spain",
                "Corp Technologies Spain S.L.",
                new BigDecimal("2500000.00"), // 2.5M revenue
                5,                            // 5 años
                List.of("ISO-9001", "ISO-27001", "ISO-14001"),
                List.of("72000000-5")
        );

        TenderOpportunity.EligibilityScore score = service.evaluateContractorForTender(tender.id(), contractor);

        assertTrue(score.isFinanciallyEligible());
        assertTrue(score.isTechnicallyEligible());
        assertEquals(100.0, score.matchScorePct());
        assertTrue(score.missingRequirements().isEmpty());
    }

    @Test
    @DisplayName("Debe penalizar el score y detallar requisitos faltantes cuando el contratista no llega a la facturación mínima")
    void shouldDetectMissingRequirementsAndPenalizeScore() {
        TenderOpportunity tender = service.publishTender(
                "LIC-2026-HIGH-BUDGET",
                "Adif Alta Velocidad",
                new BigDecimal("10000000.00"),
                List.of("45200000-9"),
                new BigDecimal("5000000.00"), // 5M requerido
                5,
                List.of("ISO-9001", "ISO-14001", "ISO-45001"),
                Instant.now().plusSeconds(86400 * 15)
        );

        TenderOpportunity.ContractorProfile smallContractor = new TenderOpportunity.ContractorProfile(
                "pyme-infra-01",
                "Pyme Infraestructuras S.L.",
                new BigDecimal("1200000.00"), // Solo 1.2M
                4,                            // 4 años (< 5 años)
                List.of("ISO-9001"),          // Falta 14001 y 45001
                List.of("45200000-9")
        );

        TenderOpportunity.EligibilityScore score = service.evaluateContractorForTender(tender.id(), smallContractor);

        assertFalse(score.isFinanciallyEligible());
        assertFalse(score.isTechnicallyEligible());
        assertTrue(score.matchScorePct() < 50.0);
        assertEquals(4, score.missingRequirements().size());
    }
}
