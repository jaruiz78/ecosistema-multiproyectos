package com.corp.proyectorutassenderismogr.infrastructure.adapter.out.persistence;

import com.corp.proyectorutassenderismogr.domain.model.RutasSenderismoGR;
import com.corp.proyectorutassenderismogr.domain.port.out.RutasSenderismoGRRepositoryPort;
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
public class InMemoryRutasSenderismoGRRepositoryAdapter implements RutasSenderismoGRRepositoryPort {

    private final ConcurrentMap<String, RutasSenderismoGR> storage = new ConcurrentHashMap<>();

    @Override
    public RutasSenderismoGR save(RutasSenderismoGR entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<RutasSenderismoGR> findById(String id, String tenantId) {
        RutasSenderismoGR entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
