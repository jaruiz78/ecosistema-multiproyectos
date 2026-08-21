package com.corp.proyectoagrifoodcoldchaintrace.infrastructure.adapter.out.persistence;

import com.corp.proyectoagrifoodcoldchaintrace.domain.model.ColdChainShipmentBatch;
import com.corp.proyectoagrifoodcoldchaintrace.domain.port.out.ColdChainShipmentBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryColdChainShipmentBatchRepositoryAdapter implements ColdChainShipmentBatchRepositoryPort {

    private final ConcurrentMap<String, ColdChainShipmentBatch> storage = new ConcurrentHashMap<>();

    @Override
    public ColdChainShipmentBatch save(ColdChainShipmentBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ColdChainShipmentBatch> findById(String id, String tenantId) {
        ColdChainShipmentBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
