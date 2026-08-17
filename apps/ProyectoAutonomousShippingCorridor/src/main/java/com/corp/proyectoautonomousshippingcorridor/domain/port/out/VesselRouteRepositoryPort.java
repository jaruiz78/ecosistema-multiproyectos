package com.corp.proyectoautonomousshippingcorridor.domain.port.out;

import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import java.util.Optional;

public interface VesselRouteRepositoryPort {
    AutonomousVesselRoute save(AutonomousVesselRoute route);
    Optional<AutonomousVesselRoute> findById(String imo);
}
