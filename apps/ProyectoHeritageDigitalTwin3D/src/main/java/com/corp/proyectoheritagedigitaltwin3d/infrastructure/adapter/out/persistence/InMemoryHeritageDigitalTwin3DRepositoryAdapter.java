package com.corp.proyectoheritagedigitaltwin3d.infrastructure.adapter.out.persistence;

import com.corp.proyectoheritagedigitaltwin3d.domain.model.HeritageDigitalTwin3D;
import com.corp.proyectoheritagedigitaltwin3d.domain.port.out.HeritageDigitalTwin3DRepositoryPort;
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
public class InMemoryHeritageDigitalTwin3DRepositoryAdapter implements HeritageDigitalTwin3DRepositoryPort {

    private final ConcurrentMap<String, HeritageDigitalTwin3D> storage = new ConcurrentHashMap<>();

    @Override
    public HeritageDigitalTwin3D save(HeritageDigitalTwin3D entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<HeritageDigitalTwin3D> findById(String id, String tenantId) {
        HeritageDigitalTwin3D entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
