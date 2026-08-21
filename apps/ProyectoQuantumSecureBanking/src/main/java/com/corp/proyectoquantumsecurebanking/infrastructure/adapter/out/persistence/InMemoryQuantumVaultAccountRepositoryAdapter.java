package com.corp.proyectoquantumsecurebanking.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumsecurebanking.domain.model.QuantumVaultAccount;
import com.corp.proyectoquantumsecurebanking.domain.port.out.QuantumVaultAccountRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryQuantumVaultAccountRepositoryAdapter implements QuantumVaultAccountRepositoryPort {

    private final ConcurrentMap<String, QuantumVaultAccount> storage = new ConcurrentHashMap<>();

    @Override
    public QuantumVaultAccount save(QuantumVaultAccount entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QuantumVaultAccount> findById(String id, String tenantId) {
        QuantumVaultAccount entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
