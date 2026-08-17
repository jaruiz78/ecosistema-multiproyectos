package com.corp.proyectodenovoplasticdegradation.infrastructure.adapter;

import com.corp.proyectodenovoplasticdegradation.application.port.out.PolymerEnzymeRepositoryPort;
import com.corp.proyectodenovoplasticdegradation.domain.PolymerDegradationEnzyme;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
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
