package com.corp.proyectoastroturismostarlight.domain.port.out;

import com.corp.proyectoastroturismostarlight.domain.model.StarlightObservationPoint;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface StarlightObservationPointRepositoryPort {
    StarlightObservationPoint save(StarlightObservationPoint entity);
    Optional<StarlightObservationPoint> findById(String id, String tenantId);
}
