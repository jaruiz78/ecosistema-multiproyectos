package com.corp.ecosystem.ruralturismo.application;

import com.corp.ecosystem.ruralturismo.domain.RuralVillageTouristHub;
import com.corp.ecosystem.ruralturismo.domain.port.RuralHubRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class RuralTurismoService {

    private final RuralHubRepositoryPort repositoryPort;

    public RuralTurismoService(RuralHubRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public RuralVillageTouristHub registerVillageHub(
            String tenantId,
            String name,
            String province,
            int inhabitants,
            int accommodations,
            int trails,
            double occupancyPct,
            double economicImpactEur
    ) {
        RuralVillageTouristHub.HubId id = new RuralVillageTouristHub.HubId("RURAL-" + System.nanoTime());
        RuralVillageTouristHub.CapacityMetrics metrics = new RuralVillageTouristHub.CapacityMetrics(
                accommodations, trails, occupancyPct, economicImpactEur
        );
        RuralVillageTouristHub hub = RuralVillageTouristHub.evaluateVillage(id, tenantId, name, province, inhabitants, metrics);
        return repositoryPort.save(hub);
    }

    public Optional<RuralVillageTouristHub> getHub(RuralVillageTouristHub.HubId id) {
        return repositoryPort.findById(id);
    }
}
