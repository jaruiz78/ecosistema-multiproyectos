package com.corp.proyectoallostericdrugdiscovery.infrastructure.adapter.out.persistence;

import com.corp.proyectoallostericdrugdiscovery.domain.model.CrypticBindingPocketVolumeToken;
import com.corp.proyectoallostericdrugdiscovery.domain.port.out.CrypticBindingPocketVolumeTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCrypticBindingPocketVolumeTokenRepositoryAdapter implements CrypticBindingPocketVolumeTokenRepositoryPort {

    private final ConcurrentMap<String, CrypticBindingPocketVolumeToken> storage = new ConcurrentHashMap<>();

    @Override
    public CrypticBindingPocketVolumeToken save(CrypticBindingPocketVolumeToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CrypticBindingPocketVolumeToken> findById(String id, String tenantId) {
        CrypticBindingPocketVolumeToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
