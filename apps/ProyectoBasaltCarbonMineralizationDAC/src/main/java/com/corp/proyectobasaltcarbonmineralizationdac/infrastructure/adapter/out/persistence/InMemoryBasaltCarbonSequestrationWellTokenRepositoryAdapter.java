package com.corp.proyectobasaltcarbonmineralizationdac.infrastructure.adapter.out.persistence;

import com.corp.proyectobasaltcarbonmineralizationdac.domain.model.BasaltCarbonSequestrationWellToken;
import com.corp.proyectobasaltcarbonmineralizationdac.domain.port.out.BasaltCarbonSequestrationWellTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryBasaltCarbonSequestrationWellTokenRepositoryAdapter implements BasaltCarbonSequestrationWellTokenRepositoryPort {

    private final ConcurrentMap<String, BasaltCarbonSequestrationWellToken> storage = new ConcurrentHashMap<>();

    @Override
    public BasaltCarbonSequestrationWellToken save(BasaltCarbonSequestrationWellToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<BasaltCarbonSequestrationWellToken> findById(String id, String tenantId) {
        BasaltCarbonSequestrationWellToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
