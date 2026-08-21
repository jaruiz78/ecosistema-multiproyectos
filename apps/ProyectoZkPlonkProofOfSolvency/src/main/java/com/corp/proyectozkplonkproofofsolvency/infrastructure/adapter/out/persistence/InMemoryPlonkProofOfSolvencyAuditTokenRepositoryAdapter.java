package com.corp.proyectozkplonkproofofsolvency.infrastructure.adapter.out.persistence;

import com.corp.proyectozkplonkproofofsolvency.domain.model.PlonkProofOfSolvencyAuditToken;
import com.corp.proyectozkplonkproofofsolvency.domain.port.out.PlonkProofOfSolvencyAuditTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryPlonkProofOfSolvencyAuditTokenRepositoryAdapter implements PlonkProofOfSolvencyAuditTokenRepositoryPort {

    private final ConcurrentMap<String, PlonkProofOfSolvencyAuditToken> storage = new ConcurrentHashMap<>();

    @Override
    public PlonkProofOfSolvencyAuditToken save(PlonkProofOfSolvencyAuditToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<PlonkProofOfSolvencyAuditToken> findById(String id, String tenantId) {
        PlonkProofOfSolvencyAuditToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
