package com.corp.ecosystem.hydrogen.application;

import com.corp.ecosystem.hydrogen.domain.HybridDesalHydrogenCluster;
import com.corp.ecosystem.hydrogen.domain.port.HybridPlantRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class GreenHydrogenDesalService {

    private final HybridPlantRepositoryPort repositoryPort;

    public GreenHydrogenDesalService(HybridPlantRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public HybridDesalHydrogenCluster registerHybridPlant(
            String tenantId,
            double electrolyzerMaxMw,
            double desalMaxM3Day,
            double solarPvMw,
            double windMw
    ) {
        HybridDesalHydrogenCluster.PlantCapacities capacities = new HybridDesalHydrogenCluster.PlantCapacities(
                electrolyzerMaxMw, desalMaxM3Day, solarPvMw, windMw
        );
        HybridDesalHydrogenCluster.CurrentOperatingState state = new HybridDesalHydrogenCluster.CurrentOperatingState(
                0.0, 45.0, 0.0, 0.0
        );
        HybridDesalHydrogenCluster.MpcDispatchSetpoint setpoint = new HybridDesalHydrogenCluster.MpcDispatchSetpoint(
                0.0, 0.0, 0.0, 0.0, 0.0
        );

        HybridDesalHydrogenCluster plant = new HybridDesalHydrogenCluster(
                new HybridDesalHydrogenCluster.PlantId("H2-DESAL-" + System.nanoTime()),
                tenantId,
                capacities,
                state,
                setpoint,
                Instant.now()
        );
        return repositoryPort.save(plant);
    }

    public HybridDesalHydrogenCluster dispatchMpc(
            HybridDesalHydrogenCluster.PlantId id,
            double availablePowerMw,
            double spotPriceEurMwh
    ) {
        HybridDesalHydrogenCluster plant = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Planta híbrida H2/Desalación no encontrada: " + id.value()));

        HybridDesalHydrogenCluster updated = plant.optimizeMpcDispatch(availablePowerMw, spotPriceEurMwh);
        return repositoryPort.save(updated);
    }

    public Optional<HybridDesalHydrogenCluster> getPlant(HybridDesalHydrogenCluster.PlantId id) {
        return repositoryPort.findById(id);
    }
}
