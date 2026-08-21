package com.corp.proyectoglaciermelticecapmonitor.infrastructure.adapter.out.persistence;

import com.corp.proyectoglaciermelticecapmonitor.domain.model.GlacierBedrockIceThicknessNode;
import com.corp.proyectoglaciermelticecapmonitor.domain.port.out.GlacierBedrockIceThicknessNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryGlacierBedrockIceThicknessNodeRepositoryAdapter implements GlacierBedrockIceThicknessNodeRepositoryPort {

    private final ConcurrentMap<String, GlacierBedrockIceThicknessNode> storage = new ConcurrentHashMap<>();

    @Override
    public GlacierBedrockIceThicknessNode save(GlacierBedrockIceThicknessNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<GlacierBedrockIceThicknessNode> findById(String id, String tenantId) {
        GlacierBedrockIceThicknessNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
