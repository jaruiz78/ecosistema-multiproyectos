package com.corp.ecosystem.microgrid.application;

import com.corp.ecosystem.microgrid.domain.IndustrialMicrogridNode;
import com.corp.ecosystem.microgrid.domain.port.MicrogridRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class IndustrialMicrogridService {

    private final MicrogridRepositoryPort repositoryPort;

    public IndustrialMicrogridService(MicrogridRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public IndustrialMicrogridNode registerMicrogrid(
            String tenantId,
            String parkName,
            double maxImportKw,
            double bessCapacityKwh,
            double bessMaxKw,
            double maxCurtailKw
    ) {
        IndustrialMicrogridNode.AssetCapacities capacities = new IndustrialMicrogridNode.AssetCapacities(
                maxImportKw, bessCapacityKwh, bessMaxKw, maxCurtailKw
        );
        IndustrialMicrogridNode.PowerFlowState state = new IndustrialMicrogridNode.PowerFlowState(
                0.0, 80.0, 50.0, 0.12
        );
        IndustrialMicrogridNode.MpcDispatchDecision decision = new IndustrialMicrogridNode.MpcDispatchDecision(
                0.0, 0.0, 0.0, 0.0, false
        );

        IndustrialMicrogridNode node = new IndustrialMicrogridNode(
                new IndustrialMicrogridNode.NodeId("MICROGRID-" + System.nanoTime()),
                tenantId,
                parkName,
                capacities,
                state,
                decision,
                Instant.now()
        );
        return repositoryPort.save(node);
    }

    public IndustrialMicrogridNode dispatchLoad(IndustrialMicrogridNode.NodeId id, double loadKw, double freqHz, double tariffEur) {
        IndustrialMicrogridNode node = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Microred industrial no encontrada: " + id.value()));

        IndustrialMicrogridNode updated = node.dispatchMpc(loadKw, freqHz, tariffEur);
        return repositoryPort.save(updated);
    }

    public Optional<IndustrialMicrogridNode> getMicrogrid(IndustrialMicrogridNode.NodeId id) {
        return repositoryPort.findById(id);
    }
}
