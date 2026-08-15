package com.corp.ecosystem.uspace.application;

import com.corp.ecosystem.uspace.domain.USpaceFlightCorridor;
import com.corp.ecosystem.uspace.domain.port.DroneFlightRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DroneAirspaceService {

    private final DroneFlightRepositoryPort repositoryPort;

    public DroneAirspaceService(DroneFlightRepositoryPort repositoryPort) {
        this.repositoryPort = Objects.requireNonNull(repositoryPort, "repositoryPort no puede ser nulo");
    }

    public USpaceFlightCorridor submitFlightPlan(
            String tenantId,
            String operatorId,
            List<USpaceFlightCorridor.AirspaceWaypoint3D> waypoints,
            USpaceFlightCorridor.AltitudeLayer layer,
            boolean simulatedAirspaceConflict
    ) {
        USpaceFlightCorridor.FlightPlanId id = new USpaceFlightCorridor.FlightPlanId("USPACE-" + System.nanoTime());
        USpaceFlightCorridor corridor = USpaceFlightCorridor.authorizeFlightPlan(
                id, tenantId, operatorId, waypoints, layer, simulatedAirspaceConflict
        );
        return repositoryPort.save(corridor);
    }

    public Optional<USpaceFlightCorridor> getFlightPlan(USpaceFlightCorridor.FlightPlanId id) {
        return repositoryPort.findById(id);
    }
}
