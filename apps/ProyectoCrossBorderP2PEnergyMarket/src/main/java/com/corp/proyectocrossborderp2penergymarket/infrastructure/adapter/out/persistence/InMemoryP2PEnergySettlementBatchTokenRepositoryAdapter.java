package com.corp.proyectocrossborderp2penergymarket.infrastructure.adapter.out.persistence;

import com.corp.proyectocrossborderp2penergymarket.domain.model.P2PEnergySettlementBatchToken;
import com.corp.proyectocrossborderp2penergymarket.domain.port.out.P2PEnergySettlementBatchTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryP2PEnergySettlementBatchTokenRepositoryAdapter implements P2PEnergySettlementBatchTokenRepositoryPort {

    private final ConcurrentMap<String, P2PEnergySettlementBatchToken> storage = new ConcurrentHashMap<>();

    @Override
    public P2PEnergySettlementBatchToken save(P2PEnergySettlementBatchToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<P2PEnergySettlementBatchToken> findById(String id, String tenantId) {
        P2PEnergySettlementBatchToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
