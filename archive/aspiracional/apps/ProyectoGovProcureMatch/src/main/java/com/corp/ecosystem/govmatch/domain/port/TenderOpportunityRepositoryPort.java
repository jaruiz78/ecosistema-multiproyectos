package com.corp.ecosystem.govmatch.domain.port;

import com.corp.ecosystem.govmatch.domain.TenderOpportunity;
import java.util.Optional;

public interface TenderOpportunityRepositoryPort {
    TenderOpportunity save(TenderOpportunity opportunity);
    Optional<TenderOpportunity> findById(TenderOpportunity.OpportunityId id);
}
