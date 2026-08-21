package com.corp.proyectocrisprprimegenetherapy.infrastructure.adapter.out.persistence;

import com.corp.proyectocrisprprimegenetherapy.domain.model.PrimeEditingTargetLocusToken;
import com.corp.proyectocrisprprimegenetherapy.domain.port.out.PrimeEditingTargetLocusTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPrimeEditingTargetLocusTokenRepositoryAdapter implements PrimeEditingTargetLocusTokenRepositoryPort {

    private final ConcurrentMap<String, PrimeEditingTargetLocusToken> storage = new ConcurrentHashMap<>();

    @Override
    public PrimeEditingTargetLocusToken save(PrimeEditingTargetLocusToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PrimeEditingTargetLocusToken> findById(String id, String tenantId) {
        PrimeEditingTargetLocusToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
