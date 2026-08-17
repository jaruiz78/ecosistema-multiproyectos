package com.corp.proyectosyntheticenzymebiofoundry.infrastructure.adapter.out.persistence;

import com.corp.proyectosyntheticenzymebiofoundry.domain.model.SyntheticEnzymeSequence;
import com.corp.proyectosyntheticenzymebiofoundry.domain.port.out.EnzymeRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

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
