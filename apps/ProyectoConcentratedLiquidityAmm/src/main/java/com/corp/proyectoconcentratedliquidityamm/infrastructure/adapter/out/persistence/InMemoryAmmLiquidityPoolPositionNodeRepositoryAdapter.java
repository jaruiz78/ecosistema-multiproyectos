package com.corp.proyectoconcentratedliquidityamm.infrastructure.adapter.out.persistence;

import com.corp.proyectoconcentratedliquidityamm.domain.model.AmmLiquidityPoolPositionNode;
import com.corp.proyectoconcentratedliquidityamm.domain.port.out.AmmLiquidityPoolPositionNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
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
