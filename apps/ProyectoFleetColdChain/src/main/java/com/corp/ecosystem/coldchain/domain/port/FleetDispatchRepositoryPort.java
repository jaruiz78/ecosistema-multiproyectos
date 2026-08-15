package com.corp.ecosystem.coldchain.domain.port;

import com.corp.ecosystem.coldchain.domain.ColdChainFleetPlan;
import java.util.Optional;

public interface FleetDispatchRepositoryPort {
    ColdChainFleetPlan save(ColdChainFleetPlan plan);
    Optional<ColdChainFleetPlan> findById(ColdChainFleetPlan.RoutePlanId id);
}
