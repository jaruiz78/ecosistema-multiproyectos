package com.corp.proyectoprogrammableofflinecbdc.infrastructure.adapter.out.persistence;

import com.corp.proyectoprogrammableofflinecbdc.domain.model.OfflineCbdcSpendProofToken;
import com.corp.proyectoprogrammableofflinecbdc.domain.port.out.OfflineCbdcSpendProofTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryOfflineCbdcSpendProofTokenRepositoryAdapter implements OfflineCbdcSpendProofTokenRepositoryPort {

    private final ConcurrentMap<String, OfflineCbdcSpendProofToken> storage = new ConcurrentHashMap<>();

    @Override
    public OfflineCbdcSpendProofToken save(OfflineCbdcSpendProofToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<OfflineCbdcSpendProofToken> findById(String id, String tenantId) {
        OfflineCbdcSpendProofToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
