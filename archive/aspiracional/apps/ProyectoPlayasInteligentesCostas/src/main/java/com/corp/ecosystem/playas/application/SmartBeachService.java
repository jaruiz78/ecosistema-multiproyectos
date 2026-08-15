package com.corp.ecosystem.playas.application;

import com.corp.ecosystem.playas.domain.SmartBeachTwin;
import com.corp.ecosystem.playas.domain.port.SmartBeachRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class SmartBeachService {

    private final SmartBeachRepositoryPort repositoryPort;

    public SmartBeachService(SmartBeachRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public SmartBeachTwin monitorBeach(
            String tenantId,
            String name,
            long h3Index,
            int currentBathers,
            int maxCapacity,
            double eColi,
            double enterococci,
            double seaTemp
    ) {
        SmartBeachTwin.BeachId id = new SmartBeachTwin.BeachId("BEACH-" + System.nanoTime());
        SmartBeachTwin.WaterQualityMetrics water = new SmartBeachTwin.WaterQualityMetrics(eColi, enterococci, seaTemp, true);
        SmartBeachTwin beach = SmartBeachTwin.updateBeachState(id, tenantId, name, h3Index, currentBathers, maxCapacity, water);
        return repositoryPort.save(beach);
    }

    public Optional<SmartBeachTwin> getBeach(SmartBeachTwin.BeachId id) {
        return repositoryPort.findById(id);
    }
}
