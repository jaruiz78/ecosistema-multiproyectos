package com.corp.proyectodroneairspace.domain.port.out;

import com.corp.proyectodroneairspace.domain.model.DroneFlightRoute;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface DroneFlightRouteRepositoryPort {
    DroneFlightRoute save(DroneFlightRoute entity);
    Optional<DroneFlightRoute> findById(String id, String tenantId);
}
