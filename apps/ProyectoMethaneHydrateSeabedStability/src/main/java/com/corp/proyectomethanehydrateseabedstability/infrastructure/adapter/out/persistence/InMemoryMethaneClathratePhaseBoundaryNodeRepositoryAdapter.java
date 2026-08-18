package com.corp.proyectomethanehydrateseabedstability.infrastructure.adapter.out.persistence;

import com.corp.proyectomethanehydrateseabedstability.domain.model.MethaneClathratePhaseBoundaryNode;
import com.corp.proyectomethanehydrateseabedstability.domain.port.out.MethaneClathratePhaseBoundaryNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
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
