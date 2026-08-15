package com.corp.ecosystem.dti.application;

import com.corp.ecosystem.dti.domain.SmartDestinationZone;
import com.corp.ecosystem.dti.domain.port.DestinationZoneRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SmartDestinationService {

    private final DestinationZoneRepositoryPort repositoryPort;

    public SmartDestinationService(DestinationZoneRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public SmartDestinationZone registerDestinationZone(
            String tenantId,
            String destinationName,
            long h3IndexRes8,
            SmartDestinationZone.ZoneType type,
            int maxVisitors,
            double maxNoiseDb,
            List<SmartDestinationZone.DispersionRoute> alternativeRoutes
    ) {
        SmartDestinationZone.CarryingCapacityLimits limits = new SmartDestinationZone.CarryingCapacityLimits(
                maxVisitors, 250.0, maxNoiseDb, 0.85
        );
        SmartDestinationZone.CurrentCrowdState state = new SmartDestinationZone.CurrentCrowdState(
                0, 45.0, 0.0, 1
        );

        SmartDestinationZone zone = new SmartDestinationZone(
                new SmartDestinationZone.ZoneId("ZONE-" + System.nanoTime()),
                tenantId,
                destinationName,
                h3IndexRes8,
                type,
                limits,
                state,
                alternativeRoutes != null ? List.copyOf(alternativeRoutes) : List.of(),
                SmartDestinationZone.ZoneAlertLevel.GREEN_OPTIMAL,
                Instant.now()
        );
        return repositoryPort.save(zone);
    }

    public SmartDestinationZone ingestCrowdTelemetry(SmartDestinationZone.ZoneId id, int estimatedVisitors, double noiseDb) {
        SmartDestinationZone zone = repositoryPort.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada: " + id.value()));

        SmartDestinationZone updated = zone.assimilateCrowdObservation(estimatedVisitors, noiseDb);
        return repositoryPort.save(updated);
    }

    public Optional<SmartDestinationZone> getZone(SmartDestinationZone.ZoneId id) {
        return repositoryPort.findById(id);
    }
}
