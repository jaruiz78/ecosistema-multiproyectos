package com.corp.proyectoprecisionsoilregen.infrastructure.adapter.out.persistence;

import com.corp.proyectoprecisionsoilregen.domain.model.SoilCarbonMeasurement;
import com.corp.proyectoprecisionsoilregen.domain.port.out.SoilCarbonMeasurementRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemorySoilCarbonMeasurementRepositoryAdapter implements SoilCarbonMeasurementRepositoryPort {

    private final ConcurrentMap<String, SoilCarbonMeasurement> storage = new ConcurrentHashMap<>();

    @Override
    public SoilCarbonMeasurement save(SoilCarbonMeasurement entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SoilCarbonMeasurement> findById(String id, String tenantId) {
        SoilCarbonMeasurement entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
