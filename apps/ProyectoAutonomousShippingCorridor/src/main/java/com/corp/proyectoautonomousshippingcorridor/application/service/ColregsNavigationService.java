package com.corp.proyectoautonomousshippingcorridor.application.service;

import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import com.corp.proyectoautonomousshippingcorridor.domain.port.out.VesselRouteRepositoryPort;

public class ColregsNavigationService {

    private final VesselRouteRepositoryPort repositoryPort;

    public ColregsNavigationService(VesselRouteRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    public AutonomousVesselRoute avoidCollision(String imo, double obstacleBearing, double distanceNm) {
        AutonomousVesselRoute route = repositoryPort.findById(imo)
                .orElseGet(() -> AutonomousVesselRoute.create(imo, "AUTONOMOUS_CONTAINER_VESSEL_01"));

        AutonomousVesselRoute updated = route.executeColregsManeuver(obstacleBearing, distanceNm);
        return repositoryPort.save(updated);
    }
}
