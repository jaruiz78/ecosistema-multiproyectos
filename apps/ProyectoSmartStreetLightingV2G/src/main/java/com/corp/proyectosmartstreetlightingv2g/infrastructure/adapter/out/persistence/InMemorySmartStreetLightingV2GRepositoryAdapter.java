package com.corp.proyectosmartstreetlightingv2g.infrastructure.adapter.out.persistence;

import com.corp.proyectosmartstreetlightingv2g.domain.model.SmartStreetLightingV2G;
import com.corp.proyectosmartstreetlightingv2g.domain.port.out.SmartStreetLightingV2GRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class InMemorySmartStreetLightingV2GRepositoryAdapter implements SmartStreetLightingV2GRepositoryPort {

    private final ConcurrentMap<String, SmartStreetLightingV2G> storage = new ConcurrentHashMap<>();

    @Override
    public SmartStreetLightingV2G save(SmartStreetLightingV2G entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<SmartStreetLightingV2G> findById(String id, String tenantId) {
        SmartStreetLightingV2G entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
