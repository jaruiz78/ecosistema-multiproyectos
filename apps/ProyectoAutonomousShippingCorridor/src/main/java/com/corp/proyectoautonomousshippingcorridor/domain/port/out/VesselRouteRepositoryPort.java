package com.corp.proyectoautonomousshippingcorridor.domain.port.out;

import com.corp.proyectoautonomousshippingcorridor.domain.model.AutonomousVesselRoute;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface VesselRouteRepositoryPort {
    AutonomousVesselRoute save(AutonomousVesselRoute route);
    Optional<AutonomousVesselRoute> findById(String imo);
}
