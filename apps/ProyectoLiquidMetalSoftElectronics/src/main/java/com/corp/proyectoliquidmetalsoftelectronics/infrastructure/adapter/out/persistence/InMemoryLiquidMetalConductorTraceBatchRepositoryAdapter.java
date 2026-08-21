package com.corp.proyectoliquidmetalsoftelectronics.infrastructure.adapter.out.persistence;

import com.corp.proyectoliquidmetalsoftelectronics.domain.model.LiquidMetalConductorTraceBatch;
import com.corp.proyectoliquidmetalsoftelectronics.domain.port.out.LiquidMetalConductorTraceBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryLiquidMetalConductorTraceBatchRepositoryAdapter implements LiquidMetalConductorTraceBatchRepositoryPort {

    private final ConcurrentMap<String, LiquidMetalConductorTraceBatch> storage = new ConcurrentHashMap<>();

    @Override
    public LiquidMetalConductorTraceBatch save(LiquidMetalConductorTraceBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<LiquidMetalConductorTraceBatch> findById(String id, String tenantId) {
        LiquidMetalConductorTraceBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
