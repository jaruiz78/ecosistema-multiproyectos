package com.corp.proyectolastmiledronedeliverygrid.infrastructure.adapter.out.persistence;

import com.corp.proyectolastmiledronedeliverygrid.domain.model.DroneDeliveryAirspaceVolumeToken;
import com.corp.proyectolastmiledronedeliverygrid.domain.port.out.DroneDeliveryAirspaceVolumeTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryDroneDeliveryAirspaceVolumeTokenRepositoryAdapter implements DroneDeliveryAirspaceVolumeTokenRepositoryPort {

    private final ConcurrentMap<String, DroneDeliveryAirspaceVolumeToken> storage = new ConcurrentHashMap<>();

    @Override
    public DroneDeliveryAirspaceVolumeToken save(DroneDeliveryAirspaceVolumeToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DroneDeliveryAirspaceVolumeToken> findById(String id, String tenantId) {
        DroneDeliveryAirspaceVolumeToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
