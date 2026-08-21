package com.corp.proyectostratosphericaerosolgeoengineering.domain.port.out;

import com.corp.proyectostratosphericaerosolgeoengineering.domain.model.StratosphericAerosolPlume;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface AerosolPlumeRepositoryPort {
    StratosphericAerosolPlume save(StratosphericAerosolPlume plume);
    Optional<StratosphericAerosolPlume> findById(String injectionId);
}
