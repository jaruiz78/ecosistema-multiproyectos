package com.corp.proyectobiometricprivacysovereignauth.infrastructure.adapter.out.persistence;

import com.corp.proyectobiometricprivacysovereignauth.domain.model.EncryptedBiometricVectorDistanceNode;
import com.corp.proyectobiometricprivacysovereignauth.domain.port.out.EncryptedBiometricVectorDistanceNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryEncryptedBiometricVectorDistanceNodeRepositoryAdapter implements EncryptedBiometricVectorDistanceNodeRepositoryPort {

    private final ConcurrentMap<String, EncryptedBiometricVectorDistanceNode> storage = new ConcurrentHashMap<>();

    @Override
    public EncryptedBiometricVectorDistanceNode save(EncryptedBiometricVectorDistanceNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EncryptedBiometricVectorDistanceNode> findById(String id, String tenantId) {
        EncryptedBiometricVectorDistanceNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
