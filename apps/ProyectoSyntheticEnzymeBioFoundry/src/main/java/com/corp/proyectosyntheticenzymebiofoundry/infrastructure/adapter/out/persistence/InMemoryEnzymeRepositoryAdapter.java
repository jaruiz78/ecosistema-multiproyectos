package com.corp.proyectosyntheticenzymebiofoundry.infrastructure.adapter.out.persistence;

import com.corp.proyectosyntheticenzymebiofoundry.domain.model.SyntheticEnzymeSequence;
import com.corp.proyectosyntheticenzymebiofoundry.domain.port.out.EnzymeRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryEnzymeRepositoryAdapter implements EnzymeRepositoryPort {

    private final Map<String, SyntheticEnzymeSequence> storage = new ConcurrentHashMap<>();

    @Override
    public SyntheticEnzymeSequence save(SyntheticEnzymeSequence sequence) {
        storage.put(sequence.enzymeDesignId(), sequence);
        return sequence;
    }

    @Override
    public Optional<SyntheticEnzymeSequence> findById(String enzymeDesignId) {
        return Optional.ofNullable(storage.get(enzymeDesignId));
    }
}
