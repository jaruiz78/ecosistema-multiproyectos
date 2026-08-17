package com.corp.proyectofusionpowergrid.infrastructure.adapter.out.persistence;

import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import com.corp.proyectofusionpowergrid.domain.port.out.TokamakPlasmaRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTokamakPlasmaRepositoryAdapter implements TokamakPlasmaRepositoryPort {

    private final Map<String, TokamakPlasmaState> store = new ConcurrentHashMap<>();

    @Override
    public TokamakPlasmaState save(TokamakPlasmaState state) {
        store.put(state.reactorId(), state);
        return state;
    }

    @Override
    public Optional<TokamakPlasmaState> findById(String reactorId) {
        return Optional.ofNullable(store.get(reactorId));
    }
}
