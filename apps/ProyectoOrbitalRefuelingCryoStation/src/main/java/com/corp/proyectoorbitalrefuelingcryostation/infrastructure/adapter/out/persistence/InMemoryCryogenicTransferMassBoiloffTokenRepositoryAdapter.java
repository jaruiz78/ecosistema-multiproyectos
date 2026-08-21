package com.corp.proyectoorbitalrefuelingcryostation.infrastructure.adapter.out.persistence;

import com.corp.proyectoorbitalrefuelingcryostation.domain.model.CryogenicTransferMassBoiloffToken;
import com.corp.proyectoorbitalrefuelingcryostation.domain.port.out.CryogenicTransferMassBoiloffTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCryogenicTransferMassBoiloffTokenRepositoryAdapter implements CryogenicTransferMassBoiloffTokenRepositoryPort {

    private final ConcurrentMap<String, CryogenicTransferMassBoiloffToken> storage = new ConcurrentHashMap<>();

    @Override
    public CryogenicTransferMassBoiloffToken save(CryogenicTransferMassBoiloffToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CryogenicTransferMassBoiloffToken> findById(String id, String tenantId) {
        CryogenicTransferMassBoiloffToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
