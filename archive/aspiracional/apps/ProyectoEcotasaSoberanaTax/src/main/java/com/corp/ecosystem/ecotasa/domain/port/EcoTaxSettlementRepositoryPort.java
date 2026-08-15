package com.corp.ecosystem.ecotasa.domain.port;

import com.corp.ecosystem.ecotasa.domain.RegionalEcoTaxSettlement;
import java.util.Optional;

public interface EcoTaxSettlementRepositoryPort {
    RegionalEcoTaxSettlement save(RegionalEcoTaxSettlement settlement);
    Optional<RegionalEcoTaxSettlement> findById(RegionalEcoTaxSettlement.SettlementId id);
}
