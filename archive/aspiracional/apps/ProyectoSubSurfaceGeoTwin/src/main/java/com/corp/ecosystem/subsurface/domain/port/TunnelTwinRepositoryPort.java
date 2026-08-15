package com.corp.ecosystem.subsurface.domain.port;

import com.corp.ecosystem.subsurface.domain.TunnelSectionGeoTwin;
import java.util.Optional;

public interface TunnelTwinRepositoryPort {
    TunnelSectionGeoTwin save(TunnelSectionGeoTwin section);
    Optional<TunnelSectionGeoTwin> findById(TunnelSectionGeoTwin.TunnelSectionId id);
}
