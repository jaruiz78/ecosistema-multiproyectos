package com.corp.proyectoquantummetrologycalibration.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantummetrologycalibration.domain.model.QuantumHallPlateauResistanceToken;
import com.corp.proyectoquantummetrologycalibration.domain.port.out.QuantumHallPlateauResistanceTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryQuantumHallPlateauResistanceTokenRepositoryAdapter implements QuantumHallPlateauResistanceTokenRepositoryPort {

    private final ConcurrentMap<String, QuantumHallPlateauResistanceToken> storage = new ConcurrentHashMap<>();

    @Override
    public QuantumHallPlateauResistanceToken save(QuantumHallPlateauResistanceToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<QuantumHallPlateauResistanceToken> findById(String id, String tenantId) {
        QuantumHallPlateauResistanceToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
