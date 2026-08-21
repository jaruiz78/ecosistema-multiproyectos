package com.corp.proyectospintronicpersistentmemory.infrastructure.adapter.out.persistence;

import com.corp.proyectospintronicpersistentmemory.domain.model.SpinTorqueTransferCacheToken;
import com.corp.proyectospintronicpersistentmemory.domain.port.out.SpinTorqueTransferCacheTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySpinTorqueTransferCacheTokenRepositoryAdapter implements SpinTorqueTransferCacheTokenRepositoryPort {

    private final ConcurrentMap<String, SpinTorqueTransferCacheToken> storage = new ConcurrentHashMap<>();

    @Override
    public SpinTorqueTransferCacheToken save(SpinTorqueTransferCacheToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SpinTorqueTransferCacheToken> findById(String id, String tenantId) {
        SpinTorqueTransferCacheToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
