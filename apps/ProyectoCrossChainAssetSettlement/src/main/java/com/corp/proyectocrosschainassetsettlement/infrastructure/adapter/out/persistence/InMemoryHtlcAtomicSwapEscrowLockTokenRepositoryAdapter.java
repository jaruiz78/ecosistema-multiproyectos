package com.corp.proyectocrosschainassetsettlement.infrastructure.adapter.out.persistence;

import com.corp.proyectocrosschainassetsettlement.domain.model.HtlcAtomicSwapEscrowLockToken;
import com.corp.proyectocrosschainassetsettlement.domain.port.out.HtlcAtomicSwapEscrowLockTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryHtlcAtomicSwapEscrowLockTokenRepositoryAdapter implements HtlcAtomicSwapEscrowLockTokenRepositoryPort {

    private final ConcurrentMap<String, HtlcAtomicSwapEscrowLockToken> storage = new ConcurrentHashMap<>();

    @Override
    public HtlcAtomicSwapEscrowLockToken save(HtlcAtomicSwapEscrowLockToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HtlcAtomicSwapEscrowLockToken> findById(String id, String tenantId) {
        HtlcAtomicSwapEscrowLockToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
