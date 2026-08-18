package com.corp.proyectomicrobialelectrosynthesisbiofuel.infrastructure.adapter.out.persistence;

import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.model.CathodeBiofilmElectronUptakeNode;
import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.out.CathodeBiofilmElectronUptakeNodeRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryCathodeBiofilmElectronUptakeNodeRepositoryAdapter implements CathodeBiofilmElectronUptakeNodeRepositoryPort {

    private final ConcurrentMap<String, CathodeBiofilmElectronUptakeNode> storage = new ConcurrentHashMap<>();

    @Override
    public CathodeBiofilmElectronUptakeNode save(CathodeBiofilmElectronUptakeNode entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<CathodeBiofilmElectronUptakeNode> findById(String id, String tenantId) {
        CathodeBiofilmElectronUptakeNode entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
