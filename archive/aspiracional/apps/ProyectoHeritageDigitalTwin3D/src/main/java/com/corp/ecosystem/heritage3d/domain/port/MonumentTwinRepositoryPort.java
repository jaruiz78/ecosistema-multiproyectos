package com.corp.ecosystem.heritage3d.domain.port;

import com.corp.ecosystem.heritage3d.domain.MonumentStructuralTwin;
import java.util.Optional;

public interface MonumentTwinRepositoryPort {
    MonumentStructuralTwin save(MonumentStructuralTwin twin);
    Optional<MonumentStructuralTwin> findById(MonumentStructuralTwin.MonumentId id);
}
