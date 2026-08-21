package com.corp.proyectodeepgeothermalenergytwin.infrastructure.adapter.out.persistence;

import com.corp.proyectodeepgeothermalenergytwin.domain.model.GeothermalBoreholeHeatExchangerNode;
import com.corp.proyectodeepgeothermalenergytwin.domain.port.out.GeothermalBoreholeHeatExchangerNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryGeothermalBoreholeHeatExchangerNodeRepositoryAdapter implements GeothermalBoreholeHeatExchangerNodeRepositoryPort {

    private final ConcurrentMap<String, GeothermalBoreholeHeatExchangerNode> storage = new ConcurrentHashMap<>();

    @Override
    public GeothermalBoreholeHeatExchangerNode save(GeothermalBoreholeHeatExchangerNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GeothermalBoreholeHeatExchangerNode> findById(String id, String tenantId) {
        GeothermalBoreholeHeatExchangerNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
