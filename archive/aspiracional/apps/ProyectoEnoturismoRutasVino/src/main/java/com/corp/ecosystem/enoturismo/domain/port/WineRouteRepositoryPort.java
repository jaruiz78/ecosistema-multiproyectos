package com.corp.ecosystem.enoturismo.domain.port;

import com.corp.ecosystem.enoturismo.domain.WineRouteExperienceTwin;
import java.util.Optional;

public interface WineRouteRepositoryPort {
    WineRouteExperienceTwin save(WineRouteExperienceTwin experience);
    Optional<WineRouteExperienceTwin> findById(WineRouteExperienceTwin.ExperienceId id);
}
