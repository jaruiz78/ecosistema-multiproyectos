package com.corp.proyectoagrowaterai.infrastructure.adapter.out.persistence;

import com.corp.proyectoagrowaterai.domain.model.WaterIrrigationPlan;
import com.corp.proyectoagrowaterai.domain.port.out.WaterIrrigationPlanRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryWaterIrrigationPlanRepositoryAdapter implements WaterIrrigationPlanRepositoryPort {

    private final ConcurrentMap<String, WaterIrrigationPlan> storage = new ConcurrentHashMap<>();

    @Override
    public WaterIrrigationPlan save(WaterIrrigationPlan entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<WaterIrrigationPlan> findById(String id, String tenantId) {
        WaterIrrigationPlan entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
