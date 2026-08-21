package com.corp.proyectointerplanetarydelaytolerantrelay.infrastructure.adapter.out.persistence;

import com.corp.proyectointerplanetarydelaytolerantrelay.domain.model.DtnBundleCustodyTransferToken;
import com.corp.proyectointerplanetarydelaytolerantrelay.domain.port.out.DtnBundleCustodyTransferTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryDtnBundleCustodyTransferTokenRepositoryAdapter implements DtnBundleCustodyTransferTokenRepositoryPort {

    private final ConcurrentMap<String, DtnBundleCustodyTransferToken> storage = new ConcurrentHashMap<>();

    @Override
    public DtnBundleCustodyTransferToken save(DtnBundleCustodyTransferToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DtnBundleCustodyTransferToken> findById(String id, String tenantId) {
        DtnBundleCustodyTransferToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
