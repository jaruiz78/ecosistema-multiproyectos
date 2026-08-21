package com.corp.proyectorecursivesnarkverifier.infrastructure.adapter.out.persistence;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import com.corp.proyectorecursivesnarkverifier.domain.port.out.Halo2ProofAggregationBatchTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryHalo2ProofAggregationBatchTokenRepositoryAdapter implements Halo2ProofAggregationBatchTokenRepositoryPort {

    private final ConcurrentMap<String, Halo2ProofAggregationBatchToken> storage = new ConcurrentHashMap<>();

    @Override
    public Halo2ProofAggregationBatchToken save(Halo2ProofAggregationBatchToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<Halo2ProofAggregationBatchToken> findById(String id, String tenantId) {
        Halo2ProofAggregationBatchToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
