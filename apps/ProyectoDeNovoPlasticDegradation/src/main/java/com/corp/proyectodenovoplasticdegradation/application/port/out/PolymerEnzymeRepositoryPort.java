package com.corp.proyectodenovoplasticdegradation.application.port.out;

import com.corp.proyectodenovoplasticdegradation.domain.PolymerDegradationEnzyme;
import java.util.Optional;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public interface PolymerEnzymeRepositoryPort {
    void save(PolymerDegradationEnzyme enzyme);
    Optional<PolymerDegradationEnzyme> findById(String enzymeId);
}
