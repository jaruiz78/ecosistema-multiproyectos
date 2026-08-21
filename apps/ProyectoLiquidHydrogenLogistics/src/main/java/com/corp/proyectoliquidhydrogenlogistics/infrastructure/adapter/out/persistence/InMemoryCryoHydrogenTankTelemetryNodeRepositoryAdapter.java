package com.corp.proyectoliquidhydrogenlogistics.infrastructure.adapter.out.persistence;

import com.corp.proyectoliquidhydrogenlogistics.domain.model.CryoHydrogenTankTelemetryNode;
import com.corp.proyectoliquidhydrogenlogistics.domain.port.out.CryoHydrogenTankTelemetryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCryoHydrogenTankTelemetryNodeRepositoryAdapter implements CryoHydrogenTankTelemetryNodeRepositoryPort {

    private final ConcurrentMap<String, CryoHydrogenTankTelemetryNode> storage = new ConcurrentHashMap<>();

    @Override
    public CryoHydrogenTankTelemetryNode save(CryoHydrogenTankTelemetryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CryoHydrogenTankTelemetryNode> findById(String id, String tenantId) {
        CryoHydrogenTankTelemetryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
