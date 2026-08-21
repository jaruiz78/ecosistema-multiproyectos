package com.corp.proyectoautonomousshippingcorridor.application.service;

import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import com.corp.proyectoautonomousshippingcorridor.domain.port.out.VesselRouteRepositoryPort;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
