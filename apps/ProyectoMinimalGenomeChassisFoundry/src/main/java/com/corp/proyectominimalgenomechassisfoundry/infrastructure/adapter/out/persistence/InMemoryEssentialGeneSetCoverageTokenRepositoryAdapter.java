package com.corp.proyectominimalgenomechassisfoundry.infrastructure.adapter.out.persistence;

import com.corp.proyectominimalgenomechassisfoundry.domain.model.EssentialGeneSetCoverageToken;
import com.corp.proyectominimalgenomechassisfoundry.domain.port.out.EssentialGeneSetCoverageTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryEssentialGeneSetCoverageTokenRepositoryAdapter implements EssentialGeneSetCoverageTokenRepositoryPort {

    private final ConcurrentMap<String, EssentialGeneSetCoverageToken> storage = new ConcurrentHashMap<>();

    @Override
    public EssentialGeneSetCoverageToken save(EssentialGeneSetCoverageToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EssentialGeneSetCoverageToken> findById(String id, String tenantId) {
        EssentialGeneSetCoverageToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
