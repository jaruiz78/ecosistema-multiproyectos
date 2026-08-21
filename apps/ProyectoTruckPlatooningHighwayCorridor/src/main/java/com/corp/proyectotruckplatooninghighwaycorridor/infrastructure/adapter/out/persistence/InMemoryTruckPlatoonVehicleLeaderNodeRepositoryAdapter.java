package com.corp.proyectotruckplatooninghighwaycorridor.infrastructure.adapter.out.persistence;

import com.corp.proyectotruckplatooninghighwaycorridor.domain.model.TruckPlatoonVehicleLeaderNode;
import com.corp.proyectotruckplatooninghighwaycorridor.domain.port.out.TruckPlatoonVehicleLeaderNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryTruckPlatoonVehicleLeaderNodeRepositoryAdapter implements TruckPlatoonVehicleLeaderNodeRepositoryPort {

    private final ConcurrentMap<String, TruckPlatoonVehicleLeaderNode> storage = new ConcurrentHashMap<>();

    @Override
    public TruckPlatoonVehicleLeaderNode save(TruckPlatoonVehicleLeaderNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<TruckPlatoonVehicleLeaderNode> findById(String id, String tenantId) {
        TruckPlatoonVehicleLeaderNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
