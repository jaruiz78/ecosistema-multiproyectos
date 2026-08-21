package com.corp.proyectoautonomousshippingcorridor.infrastructure.adapter.out.persistence;

import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import com.corp.proyectoautonomousshippingcorridor.domain.port.out.VesselRouteRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryVesselRouteRepositoryAdapter implements VesselRouteRepositoryPort {

    private final Map<String, AutonomousVesselRoute> routes = new ConcurrentHashMap<>();

    @Override
    public AutonomousVesselRoute save(AutonomousVesselRoute route) {
        routes.put(route.imoVesselNumber(), route);
        return route;
    }

    @Override
    public Optional<AutonomousVesselRoute> findById(String imo) {
        return Optional.ofNullable(routes.get(imo));
    }
}
