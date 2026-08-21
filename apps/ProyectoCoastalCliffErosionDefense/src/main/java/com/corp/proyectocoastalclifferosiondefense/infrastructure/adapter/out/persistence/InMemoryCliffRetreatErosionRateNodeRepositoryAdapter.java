package com.corp.proyectocoastalclifferosiondefense.infrastructure.adapter.out.persistence;

import com.corp.proyectocoastalclifferosiondefense.domain.model.CliffRetreatErosionRateNode;
import com.corp.proyectocoastalclifferosiondefense.domain.port.out.CliffRetreatErosionRateNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryCliffRetreatErosionRateNodeRepositoryAdapter implements CliffRetreatErosionRateNodeRepositoryPort {

    private final ConcurrentMap<String, CliffRetreatErosionRateNode> storage = new ConcurrentHashMap<>();

    @Override
    public CliffRetreatErosionRateNode save(CliffRetreatErosionRateNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CliffRetreatErosionRateNode> findById(String id, String tenantId) {
        CliffRetreatErosionRateNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
