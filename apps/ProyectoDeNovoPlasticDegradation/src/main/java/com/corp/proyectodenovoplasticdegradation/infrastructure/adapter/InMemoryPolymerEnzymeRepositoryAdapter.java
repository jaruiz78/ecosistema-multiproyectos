package com.corp.proyectodenovoplasticdegradation.infrastructure.adapter;

import com.corp.proyectodenovoplasticdegradation.application.port.out.PolymerEnzymeRepositoryPort;
import com.corp.proyectodenovoplasticdegradation.domain.PolymerDegradationEnzyme;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPolymerEnzymeRepositoryAdapter implements PolymerEnzymeRepositoryPort {

    private final Map<String, PolymerDegradationEnzyme> store = new ConcurrentHashMap<>();

    @Override
    public void save(PolymerDegradationEnzyme enzyme) {
        store.put(enzyme.enzymeId(), enzyme);
    }

    @Override
    public Optional<PolymerDegradationEnzyme> findById(String enzymeId) {
        return Optional.ofNullable(store.get(enzymeId));
    }
}
