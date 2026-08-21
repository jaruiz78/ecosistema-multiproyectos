package com.corp.proyectodesertdustairqualitygrid.infrastructure.adapter.out.persistence;

import com.corp.proyectodesertdustairqualitygrid.domain.model.MineralDustAerosolOpticalDepthNode;
import com.corp.proyectodesertdustairqualitygrid.domain.port.out.MineralDustAerosolOpticalDepthNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMineralDustAerosolOpticalDepthNodeRepositoryAdapter implements MineralDustAerosolOpticalDepthNodeRepositoryPort {

    private final ConcurrentMap<String, MineralDustAerosolOpticalDepthNode> storage = new ConcurrentHashMap<>();

    @Override
    public MineralDustAerosolOpticalDepthNode save(MineralDustAerosolOpticalDepthNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MineralDustAerosolOpticalDepthNode> findById(String id, String tenantId) {
        MineralDustAerosolOpticalDepthNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
