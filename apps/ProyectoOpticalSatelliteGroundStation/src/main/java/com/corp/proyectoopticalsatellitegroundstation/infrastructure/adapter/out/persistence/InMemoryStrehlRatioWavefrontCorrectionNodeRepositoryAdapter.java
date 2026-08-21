package com.corp.proyectoopticalsatellitegroundstation.infrastructure.adapter.out.persistence;

import com.corp.proyectoopticalsatellitegroundstation.domain.model.StrehlRatioWavefrontCorrectionNode;
import com.corp.proyectoopticalsatellitegroundstation.domain.port.out.StrehlRatioWavefrontCorrectionNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryStrehlRatioWavefrontCorrectionNodeRepositoryAdapter implements StrehlRatioWavefrontCorrectionNodeRepositoryPort {

    private final ConcurrentMap<String, StrehlRatioWavefrontCorrectionNode> storage = new ConcurrentHashMap<>();

    @Override
    public StrehlRatioWavefrontCorrectionNode save(StrehlRatioWavefrontCorrectionNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<StrehlRatioWavefrontCorrectionNode> findById(String id, String tenantId) {
        StrehlRatioWavefrontCorrectionNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
