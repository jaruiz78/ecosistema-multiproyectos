package com.corp.proyectoconcentratedliquidityamm.infrastructure.adapter.out.persistence;

import com.corp.proyectoconcentratedliquidityamm.domain.model.AmmLiquidityPoolPositionNode;
import com.corp.proyectoconcentratedliquidityamm.domain.port.out.AmmLiquidityPoolPositionNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryAmmLiquidityPoolPositionNodeRepositoryAdapter implements AmmLiquidityPoolPositionNodeRepositoryPort {

    private final ConcurrentMap<String, AmmLiquidityPoolPositionNode> storage = new ConcurrentHashMap<>();

    @Override
    public AmmLiquidityPoolPositionNode save(AmmLiquidityPoolPositionNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<AmmLiquidityPoolPositionNode> findById(String id, String tenantId) {
        AmmLiquidityPoolPositionNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
