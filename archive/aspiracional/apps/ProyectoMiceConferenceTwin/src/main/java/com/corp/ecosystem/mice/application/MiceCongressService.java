package com.corp.ecosystem.mice.application;

import com.corp.ecosystem.mice.domain.MiceEventCongressTwin;
import com.corp.ecosystem.mice.domain.port.MiceCongressRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class MiceCongressService {

    private final MiceCongressRepositoryPort repositoryPort;

    public MiceCongressService(MiceCongressRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public MiceEventCongressTwin createCongress(
            String tenantId,
            String name,
            String center,
            int attendees,
            int exhibitors,
            double economicImpactEur,
            double occupancyPct
    ) {
        MiceEventCongressTwin.CongressId id = new MiceEventCongressTwin.CongressId("MICE-" + System.nanoTime());
        MiceEventCongressTwin.CongressMetrics metrics = new MiceEventCongressTwin.CongressMetrics(
                attendees, exhibitors, economicImpactEur, occupancyPct
        );
        MiceEventCongressTwin event = MiceEventCongressTwin.registerEvent(id, tenantId, name, center, metrics, Instant.now());
        return repositoryPort.save(event);
    }

    public Optional<MiceEventCongressTwin> getCongress(MiceEventCongressTwin.CongressId id) {
        return repositoryPort.findById(id);
    }
}
