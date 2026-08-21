package com.corp.proyectoestuarinesalinityintrusiontwin.infrastructure.adapter.out.persistence;

import com.corp.proyectoestuarinesalinityintrusiontwin.domain.model.EstuarineSalinityIsohalineDistanceNode;
import com.corp.proyectoestuarinesalinityintrusiontwin.domain.port.out.EstuarineSalinityIsohalineDistanceNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryEstuarineSalinityIsohalineDistanceNodeRepositoryAdapter implements EstuarineSalinityIsohalineDistanceNodeRepositoryPort {

    private final ConcurrentMap<String, EstuarineSalinityIsohalineDistanceNode> storage = new ConcurrentHashMap<>();

    @Override
    public EstuarineSalinityIsohalineDistanceNode save(EstuarineSalinityIsohalineDistanceNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<EstuarineSalinityIsohalineDistanceNode> findById(String id, String tenantId) {
        EstuarineSalinityIsohalineDistanceNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
