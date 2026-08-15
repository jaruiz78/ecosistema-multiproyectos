package com.corp.ecosystem.balnearios.application;

import com.corp.ecosystem.balnearios.domain.ThermalSpaSpringTwin;
import com.corp.ecosystem.balnearios.domain.port.ThermalSpaRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ThermalSpaService {

    private final ThermalSpaRepositoryPort repositoryPort;

    public ThermalSpaService(ThermalSpaRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public ThermalSpaSpringTwin monitorThermalSpring(
            String tenantId,
            String spaName,
            String community,
            double waterTempCelsius,
            double dryResidueMgL,
            double flowRateLps,
            int bathersCount,
            int maxPoolCapacity
    ) {
        ThermalSpaSpringTwin.SpaSpringId id = new ThermalSpaSpringTwin.SpaSpringId("SPA-" + System.nanoTime());
        ThermalSpaSpringTwin.MineralWaterMetrics metrics = new ThermalSpaSpringTwin.MineralWaterMetrics(
                waterTempCelsius, dryResidueMgL, flowRateLps, bathersCount, maxPoolCapacity
        );
        ThermalSpaSpringTwin twin = ThermalSpaSpringTwin.inspectSpring(id, tenantId, spaName, community, metrics);
        return repositoryPort.save(twin);
    }

    public Optional<ThermalSpaSpringTwin> getSpa(ThermalSpaSpringTwin.SpaSpringId id) {
        return repositoryPort.findById(id);
    }
}
