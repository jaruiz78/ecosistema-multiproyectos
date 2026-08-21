package com.corp.proyectofusionnuclearmhd.domain.port.out;

import com.corp.proyectofusionnuclearmhd.domain.model.PlasmaConfinementZone;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface PlasmaConfinementZoneRepositoryPort {
    PlasmaConfinementZone save(PlasmaConfinementZone entity);
    Optional<PlasmaConfinementZone> findById(String id, String tenantId);
}
