package com.corp.proyectoconfidentialdatacleanroom.infrastructure.adapter.out.persistence;

import com.corp.proyectoconfidentialdatacleanroom.domain.model.SecureEnclaveAnalyticsAttestationToken;
import com.corp.proyectoconfidentialdatacleanroom.domain.port.out.SecureEnclaveAnalyticsAttestationTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySecureEnclaveAnalyticsAttestationTokenRepositoryAdapter implements SecureEnclaveAnalyticsAttestationTokenRepositoryPort {

    private final ConcurrentMap<String, SecureEnclaveAnalyticsAttestationToken> storage = new ConcurrentHashMap<>();

    @Override
    public SecureEnclaveAnalyticsAttestationToken save(SecureEnclaveAnalyticsAttestationToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SecureEnclaveAnalyticsAttestationToken> findById(String id, String tenantId) {
        SecureEnclaveAnalyticsAttestationToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
