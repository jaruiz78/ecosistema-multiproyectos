package com.corp.ecosystem.presatwin.application;

import com.corp.ecosystem.presatwin.domain.DamHydroTwinNode;
import com.corp.ecosystem.presatwin.domain.port.DamTelemetryRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DamHydroTwinService {

    private final DamTelemetryRepositoryPort repositoryPort;

    public DamHydroTwinService(DamTelemetryRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public DamHydroTwinNode registerDam(
            String tenantId,
            String damName,
            double maxCapHm3,
            double floodStorageHm3,
            double crestMeters,
            double spillwayM3s,
            double currentVolumeHm3,
            double currentLevelMeters
    ) {
        DamHydroTwinNode.ReservoirCapacity capacity = new DamHydroTwinNode.ReservoirCapacity(
                maxCapHm3, floodStorageHm3, crestMeters, spillwayM3s
        );
        DamHydroTwinNode.StructuralHealth health = new DamHydroTwinNode.StructuralHealth(2.5, 4.0, 1.2, true);
        DamHydroTwinNode.CurrentHydroState state = new DamHydroTwinNode.CurrentHydroState(
                currentVolumeHm3, currentLevelMeters, 25.0, 0.0, 10.0
        );

        DamHydroTwinNode dam = new DamHydroTwinNode(
                new DamHydroTwinNode.DamId("DAM-" + System.nanoTime()),
                tenantId,
                damName,
                capacity,
                health,
                state,
                List.of(),
                DamHydroTwinNode.DamSafetyStatus.NORMAL,
                Instant.now()
        );
        return repositoryPort.save(dam);
    }

    public DamHydroTwinNode assimilateScadaTelemetry(
            DamHydroTwinNode.DamId id,
            double waterLevelMeters,
            double inflowM3s,
            double porePressureBar
    ) {
        DamHydroTwinNode dam = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Presa no encontrada: " + id.value()));

        DamHydroTwinNode updated = dam.assimilateObservation(waterLevelMeters, inflowM3s, porePressureBar);
        return repositoryPort.save(updated);
    }

    public Optional<DamHydroTwinNode> getDam(DamHydroTwinNode.DamId id) {
        return repositoryPort.findById(id);
    }
}
