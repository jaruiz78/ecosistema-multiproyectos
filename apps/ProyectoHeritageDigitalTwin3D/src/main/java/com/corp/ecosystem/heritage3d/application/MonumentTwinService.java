package com.corp.ecosystem.heritage3d.application;

import com.corp.ecosystem.heritage3d.domain.MonumentStructuralTwin;
import com.corp.ecosystem.heritage3d.domain.port.MonumentTwinRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class MonumentTwinService {

    private final MonumentTwinRepositoryPort repositoryPort;

    public MonumentTwinService(MonumentTwinRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public MonumentStructuralTwin recordScan(
            String tenantId,
            String monumentName,
            long pointsCount,
            double crackDisplacementMm,
            double moisturePct,
            double vibrationHz,
            double maxDisplacementMm
    ) {
        MonumentStructuralTwin.MonumentId id = new MonumentStructuralTwin.MonumentId("MONUMENT-" + System.nanoTime());
        MonumentStructuralTwin.StructuralHealthMetrics metrics = new MonumentStructuralTwin.StructuralHealthMetrics(
                crackDisplacementMm, moisturePct, vibrationHz, maxDisplacementMm
        );
        MonumentStructuralTwin twin = MonumentStructuralTwin.analyzeScan(id, tenantId, monumentName, pointsCount, metrics);
        return repositoryPort.save(twin);
    }

    public Optional<MonumentStructuralTwin> getMonument(MonumentStructuralTwin.MonumentId id) {
        return repositoryPort.findById(id);
    }
}
