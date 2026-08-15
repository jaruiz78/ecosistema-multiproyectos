package com.corp.ecosystem.agroenergy.domain.port;

import com.corp.ecosystem.agroenergy.domain.AgroEnergyCommunity;
import java.util.Optional;

public interface AgroEnergyRepositoryPort {
    AgroEnergyCommunity save(AgroEnergyCommunity community);
    Optional<AgroEnergyCommunity> findById(AgroEnergyCommunity.CommunityId id);
}
