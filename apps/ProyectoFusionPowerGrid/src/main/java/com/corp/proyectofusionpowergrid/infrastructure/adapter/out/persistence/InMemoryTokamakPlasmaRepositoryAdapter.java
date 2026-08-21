package com.corp.proyectofusionpowergrid.infrastructure.adapter.out.persistence;

import com.corp.proyectofusionpowergrid.domain.model.TokamakPlasmaState;
import com.corp.proyectofusionpowergrid.domain.port.out.TokamakPlasmaRepositoryPort;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
