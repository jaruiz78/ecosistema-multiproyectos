package com.corp.proyectocircadianlightingsmartcity.infrastructure.adapter.out.persistence;

import com.corp.proyectocircadianlightingsmartcity.domain.model.CircadianLightingMelanopicNode;
import com.corp.proyectocircadianlightingsmartcity.domain.port.out.CircadianLightingMelanopicNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCircadianLightingMelanopicNodeRepositoryAdapter implements CircadianLightingMelanopicNodeRepositoryPort {

    private final ConcurrentMap<String, CircadianLightingMelanopicNode> storage = new ConcurrentHashMap<>();

    @Override
    public CircadianLightingMelanopicNode save(CircadianLightingMelanopicNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CircadianLightingMelanopicNode> findById(String id, String tenantId) {
        CircadianLightingMelanopicNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
