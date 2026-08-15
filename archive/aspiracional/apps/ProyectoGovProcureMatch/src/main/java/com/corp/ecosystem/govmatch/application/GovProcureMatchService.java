package com.corp.ecosystem.govmatch.application;

import com.corp.ecosystem.govmatch.domain.TenderOpportunity;
import com.corp.ecosystem.govmatch.domain.port.TenderOpportunityRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GovProcureMatchService {

    private final TenderOpportunityRepositoryPort repositoryPort;

    public GovProcureMatchService(TenderOpportunityRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public TenderOpportunity publishTender(
            String tenderRefCode,
            String contractingAuthority,
            BigDecimal budgetEur,
            List<String> cpvCodes,
            BigDecimal minRevenueEur,
            int minExperienceYears,
            List<String> requiredIsoList,
            Instant deadline
    ) {
        TenderOpportunity.SolvencyCriteria solvency = new TenderOpportunity.SolvencyCriteria(
                minRevenueEur, minExperienceYears, requiredIsoList != null ? List.copyOf(requiredIsoList) : List.of(), 50.0
        );

        TenderOpportunity opp = new TenderOpportunity(
                new TenderOpportunity.OpportunityId("TENDER-" + System.nanoTime()),
                tenderRefCode,
                contractingAuthority,
                budgetEur,
                cpvCodes != null ? List.copyOf(cpvCodes) : List.of(),
                solvency,
                TenderOpportunity.OpportunityState.PUBLISHED,
                deadline
        );
        return repositoryPort.save(opp);
    }

    public TenderOpportunity.EligibilityScore evaluateContractorForTender(
            TenderOpportunity.OpportunityId id,
            TenderOpportunity.ContractorProfile contractor
    ) {
        TenderOpportunity tender = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Licitación no encontrada: " + id.value()));

        return tender.evaluateContractor(contractor);
    }

    public Optional<TenderOpportunity> getTender(TenderOpportunity.OpportunityId id) {
        return repositoryPort.findById(id);
    }
}
