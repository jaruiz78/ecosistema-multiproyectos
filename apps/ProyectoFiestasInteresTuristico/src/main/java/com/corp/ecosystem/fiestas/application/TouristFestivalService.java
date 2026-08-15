package com.corp.ecosystem.fiestas.application;

import com.corp.ecosystem.fiestas.domain.TouristFestivalTwin;
import com.corp.ecosystem.fiestas.domain.port.TouristFestivalRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Service
public class TouristFestivalService {

    private final TouristFestivalRepositoryPort repositoryPort;

    public TouristFestivalService(TouristFestivalRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public TouristFestivalTwin scheduleFestival(
            String tenantId,
            String name,
            String category,
            int congregationCount,
            int maxCapacity,
            int evacuationCorridors,
            boolean routesClear
    ) {
        TouristFestivalTwin.FestivalId id = new TouristFestivalTwin.FestivalId("FESTIVAL-" + System.nanoTime());
        TouristFestivalTwin.FestivalSafetyMetrics metrics = new TouristFestivalTwin.FestivalSafetyMetrics(
                congregationCount, maxCapacity, evacuationCorridors, routesClear
        );
        TouristFestivalTwin festival = TouristFestivalTwin.planFestival(id, tenantId, name, category, metrics, Instant.now());
        return repositoryPort.save(festival);
    }

    public Optional<TouristFestivalTwin> getFestival(TouristFestivalTwin.FestivalId id) {
        return repositoryPort.findById(id);
    }
}
