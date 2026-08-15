package com.corp.ecosystem.intermodal.application;

import com.corp.ecosystem.intermodal.domain.IntermodalTransferHub;
import com.corp.ecosystem.intermodal.domain.port.IntermodalHubRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IntermodalHubService {

    private final IntermodalHubRepositoryPort repositoryPort;

    public IntermodalHubService(IntermodalHubRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public IntermodalTransferHub registerTerminalHub(
            String tenantId,
            String terminalName,
            IntermodalTransferHub.HubType type,
            int minibuses,
            int vans,
            int taxis
    ) {
        IntermodalTransferHub.FleetAvailability fleet = new IntermodalTransferHub.FleetAvailability(
                minibuses, vans, taxis
        );

        IntermodalTransferHub hub = new IntermodalTransferHub(
                new IntermodalTransferHub.HubId("HUB-" + System.nanoTime()),
                tenantId,
                terminalName,
                type,
                List.of(),
                fleet,
                List.of(),
                Instant.now()
        );
        return repositoryPort.save(hub);
    }

    public IntermodalTransferHub dispatchTransfers(
            IntermodalTransferHub.HubId id,
            String carrierId,
            long targetH3IndexRes8,
            String destinationCluster,
            int passengerCount
    ) {
        IntermodalTransferHub hub = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Terminal Hub no encontrado: " + id.value()));

        IntermodalTransferHub updated = hub.createDispatchForArrival(carrierId, targetH3IndexRes8, destinationCluster, passengerCount);
        return repositoryPort.save(updated);
    }

    public Optional<IntermodalTransferHub> getHub(IntermodalTransferHub.HubId id) {
        return repositoryPort.findById(id);
    }
}
