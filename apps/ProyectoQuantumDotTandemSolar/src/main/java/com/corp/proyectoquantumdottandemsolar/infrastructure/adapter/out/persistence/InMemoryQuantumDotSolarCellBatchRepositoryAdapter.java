package com.corp.proyectoquantumdottandemsolar.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumdottandemsolar.domain.model.QuantumDotSolarCellBatch;
import com.corp.proyectoquantumdottandemsolar.domain.port.out.QuantumDotSolarCellBatchRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryQuantumDotSolarCellBatchRepositoryAdapter implements QuantumDotSolarCellBatchRepositoryPort {

    private final ConcurrentMap<String, QuantumDotSolarCellBatch> storage = new ConcurrentHashMap<>();

    @Override
    public QuantumDotSolarCellBatch save(QuantumDotSolarCellBatch entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QuantumDotSolarCellBatch> findById(String id, String tenantId) {
        QuantumDotSolarCellBatch entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
