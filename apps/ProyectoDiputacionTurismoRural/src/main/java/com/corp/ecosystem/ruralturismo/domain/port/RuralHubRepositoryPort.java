package com.corp.ecosystem.ruralturismo.domain.port;

import com.corp.ecosystem.ruralturismo.domain.RuralVillageTouristHub;
import java.util.Optional;

public interface RuralHubRepositoryPort {
    RuralVillageTouristHub save(RuralVillageTouristHub hub);
    Optional<RuralVillageTouristHub> findById(RuralVillageTouristHub.HubId id);
}
