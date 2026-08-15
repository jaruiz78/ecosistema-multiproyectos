package com.corp.ecosystem.smartlighting.application;

import com.corp.ecosystem.smartlighting.domain.StreetLightingSegmentCluster;
import com.corp.ecosystem.smartlighting.domain.port.LightingSegmentRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class SmartLightingV2gService {

    private final LightingSegmentRepositoryPort repositoryPort;

    public SmartLightingV2gService(LightingSegmentRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public StreetLightingSegmentCluster registerSegment(
            String tenantId,
            long h3Index,
            int luminaireCount
    ) {
        StreetLightingSegmentCluster.LightingOperatingState light = new StreetLightingSegmentCluster.LightingOperatingState(
                20.0, 0, 0, 100.0
        );
        StreetLightingSegmentCluster.V2gChargingHubState v2g = new StreetLightingSegmentCluster.V2gChargingHubState(
                0, 0.0, 0.0
        );
        StreetLightingSegmentCluster.EnergyOptimizationDecision decision = new StreetLightingSegmentCluster.EnergyOptimizationDecision(
                20.0, 80.0, 0.0
        );

        StreetLightingSegmentCluster cluster = new StreetLightingSegmentCluster(
                new StreetLightingSegmentCluster.SegmentId("LIGHT-SEG-" + System.nanoTime()),
                tenantId,
                h3Index,
                luminaireCount,
                light,
                v2g,
                decision,
                Instant.now()
        );
        return repositoryPort.save(cluster);
    }

    public StreetLightingSegmentCluster updateSensoryConditions(
            StreetLightingSegmentCluster.SegmentId id,
            int pedestrians,
            int vehicles,
            double ambientLux,
            int connectedEvs,
            double tariffEur
    ) {
        StreetLightingSegmentCluster cluster = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Segmento de alumbrado no encontrado: " + id.value()));

        StreetLightingSegmentCluster updated = cluster.adjustLightingAndV2G(pedestrians, vehicles, ambientLux, connectedEvs, tariffEur);
        return repositoryPort.save(updated);
    }

    public Optional<StreetLightingSegmentCluster> getSegment(StreetLightingSegmentCluster.SegmentId id) {
        return repositoryPort.findById(id);
    }
}
