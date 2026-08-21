package com.corp.proyectobacterialquoruminhibition.infrastructure.adapter.out.persistence;

import com.corp.proyectobacterialquoruminhibition.domain.model.AutoinducerSignalingBlockadeNode;
import com.corp.proyectobacterialquoruminhibition.domain.port.out.AutoinducerSignalingBlockadeNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAutoinducerSignalingBlockadeNodeRepositoryAdapter implements AutoinducerSignalingBlockadeNodeRepositoryPort {

    private final ConcurrentMap<String, AutoinducerSignalingBlockadeNode> storage = new ConcurrentHashMap<>();

    @Override
    public AutoinducerSignalingBlockadeNode save(AutoinducerSignalingBlockadeNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AutoinducerSignalingBlockadeNode> findById(String id, String tenantId) {
        AutoinducerSignalingBlockadeNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
