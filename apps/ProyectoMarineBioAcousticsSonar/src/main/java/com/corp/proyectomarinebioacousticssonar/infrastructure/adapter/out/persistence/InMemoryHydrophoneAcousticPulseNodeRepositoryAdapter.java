package com.corp.proyectomarinebioacousticssonar.infrastructure.adapter.out.persistence;

import com.corp.proyectomarinebioacousticssonar.domain.model.HydrophoneAcousticPulseNode;
import com.corp.proyectomarinebioacousticssonar.domain.port.out.HydrophoneAcousticPulseNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryHydrophoneAcousticPulseNodeRepositoryAdapter implements HydrophoneAcousticPulseNodeRepositoryPort {

    private final ConcurrentMap<String, HydrophoneAcousticPulseNode> storage = new ConcurrentHashMap<>();

    @Override
    public HydrophoneAcousticPulseNode save(HydrophoneAcousticPulseNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HydrophoneAcousticPulseNode> findById(String id, String tenantId) {
        HydrophoneAcousticPulseNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
