package com.corp.ecosystem.senderismo.domain.port;

import com.corp.ecosystem.senderismo.domain.HikingTrailSegmentTwin;
import java.util.Optional;

public interface HikingTrailRepositoryPort {
    HikingTrailSegmentTwin save(HikingTrailSegmentTwin trail);
    Optional<HikingTrailSegmentTwin> findById(HikingTrailSegmentTwin.TrailSegmentId id);
}
