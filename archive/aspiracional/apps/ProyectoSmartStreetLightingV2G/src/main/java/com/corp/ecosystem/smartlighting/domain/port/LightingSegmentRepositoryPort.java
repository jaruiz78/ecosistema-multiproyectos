package com.corp.ecosystem.smartlighting.domain.port;

import com.corp.ecosystem.smartlighting.domain.StreetLightingSegmentCluster;
import java.util.Optional;

public interface LightingSegmentRepositoryPort {
    StreetLightingSegmentCluster save(StreetLightingSegmentCluster cluster);
    Optional<StreetLightingSegmentCluster> findById(StreetLightingSegmentCluster.SegmentId id);
}
