package com.corp.proyectolunargatewayorbitstation.infrastructure.adapter.out.persistence;

import com.corp.proyectolunargatewayorbitstation.domain.model.NrhoJacobiConstantStabilityNode;
import com.corp.proyectolunargatewayorbitstation.domain.port.out.NrhoJacobiConstantStabilityNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryNrhoJacobiConstantStabilityNodeRepositoryAdapter implements NrhoJacobiConstantStabilityNodeRepositoryPort {

    private final ConcurrentMap<String, NrhoJacobiConstantStabilityNode> storage = new ConcurrentHashMap<>();

    @Override
    public NrhoJacobiConstantStabilityNode save(NrhoJacobiConstantStabilityNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<NrhoJacobiConstantStabilityNode> findById(String id, String tenantId) {
        NrhoJacobiConstantStabilityNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
