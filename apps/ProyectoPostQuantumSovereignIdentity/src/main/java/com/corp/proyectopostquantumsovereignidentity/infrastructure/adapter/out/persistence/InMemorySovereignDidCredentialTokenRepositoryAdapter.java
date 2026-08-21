package com.corp.proyectopostquantumsovereignidentity.infrastructure.adapter.out.persistence;

import com.corp.proyectopostquantumsovereignidentity.domain.model.SovereignDidCredentialToken;
import com.corp.proyectopostquantumsovereignidentity.domain.port.out.SovereignDidCredentialTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySovereignDidCredentialTokenRepositoryAdapter implements SovereignDidCredentialTokenRepositoryPort {

    private final ConcurrentMap<String, SovereignDidCredentialToken> storage = new ConcurrentHashMap<>();

    @Override
    public SovereignDidCredentialToken save(SovereignDidCredentialToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SovereignDidCredentialToken> findById(String id, String tenantId) {
        SovereignDidCredentialToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
