package com.corp.ecosystem.enoturismo.application;

import com.corp.ecosystem.enoturismo.domain.WineRouteExperienceTwin;
import com.corp.ecosystem.enoturismo.domain.port.WineRouteRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class WineRouteService {

    private final WineRouteRepositoryPort repositoryPort;

    public WineRouteService(WineRouteRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public WineRouteExperienceTwin bookWineExperience(
            String tenantId,
            String doRegion,
            String wineryName,
            int visitors,
            int maxCap,
            double sommelierScore,
            double salesEur
    ) {
        WineRouteExperienceTwin.ExperienceId id = new WineRouteExperienceTwin.ExperienceId("WINE-" + System.nanoTime());
        WineRouteExperienceTwin.WineryTourismMetrics metrics = new WineRouteExperienceTwin.WineryTourismMetrics(
                visitors, maxCap, sommelierScore, salesEur
        );
        WineRouteExperienceTwin exp = WineRouteExperienceTwin.scheduleExperience(id, tenantId, doRegion, wineryName, metrics);
        return repositoryPort.save(exp);
    }

    public Optional<WineRouteExperienceTwin> getExperience(WineRouteExperienceTwin.ExperienceId id) {
        return repositoryPort.findById(id);
    }
}
