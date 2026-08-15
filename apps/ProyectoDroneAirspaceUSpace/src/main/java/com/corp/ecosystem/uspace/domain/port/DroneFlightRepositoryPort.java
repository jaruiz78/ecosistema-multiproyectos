package com.corp.ecosystem.uspace.domain.port;

import com.corp.ecosystem.uspace.domain.USpaceFlightCorridor;
import java.util.Optional;

public interface DroneFlightRepositoryPort {
    USpaceFlightCorridor save(USpaceFlightCorridor corridor);
    Optional<USpaceFlightCorridor> findById(USpaceFlightCorridor.FlightPlanId id);
}
