package com.corp.ecosystem.balnearios.domain.port;

import com.corp.ecosystem.balnearios.domain.ThermalSpaSpringTwin;
import java.util.Optional;

public interface ThermalSpaRepositoryPort {
    ThermalSpaSpringTwin save(ThermalSpaSpringTwin twin);
    Optional<ThermalSpaSpringTwin> findById(ThermalSpaSpringTwin.SpaSpringId id);
}
