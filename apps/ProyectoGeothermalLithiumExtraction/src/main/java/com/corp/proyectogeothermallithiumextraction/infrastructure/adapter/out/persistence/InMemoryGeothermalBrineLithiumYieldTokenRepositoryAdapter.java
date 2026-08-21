package com.corp.proyectogeothermallithiumextraction.infrastructure.adapter.out.persistence;

import com.corp.proyectogeothermallithiumextraction.domain.model.GeothermalBrineLithiumYieldToken;
import com.corp.proyectogeothermallithiumextraction.domain.port.out.GeothermalBrineLithiumYieldTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryGeothermalBrineLithiumYieldTokenRepositoryAdapter implements GeothermalBrineLithiumYieldTokenRepositoryPort {

    private final ConcurrentMap<String, GeothermalBrineLithiumYieldToken> storage = new ConcurrentHashMap<>();

    @Override
    public GeothermalBrineLithiumYieldToken save(GeothermalBrineLithiumYieldToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GeothermalBrineLithiumYieldToken> findById(String id, String tenantId) {
        GeothermalBrineLithiumYieldToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
