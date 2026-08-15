package com.corp.ecosystem.presatwin.domain.port;

import com.corp.ecosystem.presatwin.domain.DamHydroTwinNode;
import java.util.Optional;

public interface DamTelemetryRepositoryPort {
    DamHydroTwinNode save(DamHydroTwinNode dam);
    Optional<DamHydroTwinNode> findById(DamHydroTwinNode.DamId id);
}
