package com.corp.ecosystem.senderismo.application;

import com.corp.ecosystem.senderismo.domain.HikingTrailSegmentTwin;
import com.corp.ecosystem.senderismo.domain.port.HikingTrailRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class HikingTrailService {

    private final HikingTrailRepositoryPort repositoryPort;

    public HikingTrailService(HikingTrailRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public HikingTrailSegmentTwin updateTrailSegment(
            String tenantId,
            String trailCodeName,
            int distanceMeters,
            int elevationGainMeters,
            int hikersCount,
            double windKmh,
            double precipMm,
            boolean landslideAlert,
            int sheltersCount
    ) {
        HikingTrailSegmentTwin.TrailSegmentId id = new HikingTrailSegmentTwin.TrailSegmentId("TRAIL-" + System.nanoTime());
        HikingTrailSegmentTwin.TrailSafetyMetrics metrics = new HikingTrailSegmentTwin.TrailSafetyMetrics(
                hikersCount, windKmh, precipMm, landslideAlert, sheltersCount
        );
        HikingTrailSegmentTwin trail = HikingTrailSegmentTwin.evaluateSegment(id, tenantId, trailCodeName, distanceMeters, elevationGainMeters, metrics);
        return repositoryPort.save(trail);
    }

    public Optional<HikingTrailSegmentTwin> getTrail(HikingTrailSegmentTwin.TrailSegmentId id) {
        return repositoryPort.findById(id);
    }
}
