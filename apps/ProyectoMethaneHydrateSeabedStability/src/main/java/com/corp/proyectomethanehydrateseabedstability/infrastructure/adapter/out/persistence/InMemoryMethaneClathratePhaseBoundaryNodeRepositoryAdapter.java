package com.corp.proyectomethanehydrateseabedstability.infrastructure.adapter.out.persistence;

import com.corp.proyectomethanehydrateseabedstability.domain.model.MethaneClathratePhaseBoundaryNode;
import com.corp.proyectomethanehydrateseabedstability.domain.port.out.MethaneClathratePhaseBoundaryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class InMemoryMethaneClathratePhaseBoundaryNodeRepositoryAdapter implements MethaneClathratePhaseBoundaryNodeRepositoryPort {

    private final ConcurrentMap<String, MethaneClathratePhaseBoundaryNode> storage = new ConcurrentHashMap<>();

    @Override
    public MethaneClathratePhaseBoundaryNode save(MethaneClathratePhaseBoundaryNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<MethaneClathratePhaseBoundaryNode> findById(String id, String tenantId) {
        MethaneClathratePhaseBoundaryNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
