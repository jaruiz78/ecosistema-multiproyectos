package com.corp.proyectozeroknowledgecreditrating.infrastructure.adapter.out.persistence;

import com.corp.proyectozeroknowledgecreditrating.domain.model.ZkCreditEligibilityProofToken;
import com.corp.proyectozeroknowledgecreditrating.domain.port.out.ZkCreditEligibilityProofTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryZkCreditEligibilityProofTokenRepositoryAdapter implements ZkCreditEligibilityProofTokenRepositoryPort {

    private final ConcurrentMap<String, ZkCreditEligibilityProofToken> storage = new ConcurrentHashMap<>();

    @Override
    public ZkCreditEligibilityProofToken save(ZkCreditEligibilityProofToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ZkCreditEligibilityProofToken> findById(String id, String tenantId) {
        ZkCreditEligibilityProofToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
