package com.corp.proyectonuclearthermalpropulsiontwin.infrastructure.adapter.out.persistence;

import com.corp.proyectonuclearthermalpropulsiontwin.domain.model.NtpSpecificImpulseThrustVectorNode;
import com.corp.proyectonuclearthermalpropulsiontwin.domain.port.out.NtpSpecificImpulseThrustVectorNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryNtpSpecificImpulseThrustVectorNodeRepositoryAdapter implements NtpSpecificImpulseThrustVectorNodeRepositoryPort {

    private final ConcurrentMap<String, NtpSpecificImpulseThrustVectorNode> storage = new ConcurrentHashMap<>();

    @Override
    public NtpSpecificImpulseThrustVectorNode save(NtpSpecificImpulseThrustVectorNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<NtpSpecificImpulseThrustVectorNode> findById(String id, String tenantId) {
        NtpSpecificImpulseThrustVectorNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
