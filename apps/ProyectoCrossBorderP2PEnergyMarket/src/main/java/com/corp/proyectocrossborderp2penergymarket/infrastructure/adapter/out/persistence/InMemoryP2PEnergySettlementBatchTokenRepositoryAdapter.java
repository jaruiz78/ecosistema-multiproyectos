package com.corp.proyectocrossborderp2penergymarket.infrastructure.adapter.out.persistence;

import com.corp.proyectocrossborderp2penergymarket.domain.model.P2PEnergySettlementBatchToken;
import com.corp.proyectocrossborderp2penergymarket.domain.port.out.P2PEnergySettlementBatchTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
