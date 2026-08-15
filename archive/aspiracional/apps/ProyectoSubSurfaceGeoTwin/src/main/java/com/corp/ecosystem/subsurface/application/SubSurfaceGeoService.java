package com.corp.ecosystem.subsurface.application;

import com.corp.ecosystem.subsurface.domain.TunnelSectionGeoTwin;
import com.corp.ecosystem.subsurface.domain.port.TunnelTwinRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubSurfaceGeoService {

    private final TunnelTwinRepositoryPort repositoryPort;

    public SubSurfaceGeoService(TunnelTwinRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public TunnelSectionGeoTwin registerTunnelSection(
            String tenantId,
            String infrastructureName,
            double chainageKm,
            double maxConvergenceMm,
            double maxPressureKpa
    ) {
        TunnelSectionGeoTwin.GeotechnicalThresholds thresholds = new TunnelSectionGeoTwin.GeotechnicalThresholds(
                maxConvergenceMm, maxPressureKpa, 1500.0
        );
        TunnelSectionGeoTwin.CurrentSensorTelemetry telemetry = new TunnelSectionGeoTwin.CurrentSensorTelemetry(
                0.0, 0.0, 0.0, System.currentTimeMillis()
        );

        TunnelSectionGeoTwin section = new TunnelSectionGeoTwin(
                new TunnelSectionGeoTwin.TunnelSectionId("TUNNEL-" + System.nanoTime()),
                tenantId,
                infrastructureName,
                chainageKm,
                thresholds,
                telemetry,
                TunnelSectionGeoTwin.StructuralHealthStatus.STABLE_NORMAL,
                Instant.now()
        );
        return repositoryPort.save(section);
    }

    public TunnelSectionGeoTwin ingestSensorTelemetry(
            TunnelSectionGeoTwin.TunnelSectionId id,
            double convergenceMm,
            double pressureKpa,
            double strain
    ) {
        TunnelSectionGeoTwin section = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sección de túnel no encontrada: " + id.value()));

        TunnelSectionGeoTwin updated = section.recordSensorReadings(convergenceMm, pressureKpa, strain);
        return repositoryPort.save(updated);
    }

    public Optional<TunnelSectionGeoTwin> getTunnelSection(TunnelSectionGeoTwin.TunnelSectionId id) {
        return repositoryPort.findById(id);
    }
}
