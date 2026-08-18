package com.corp.proyectoquantumkeyescrowsharding.infrastructure.adapter.out.persistence;

import com.corp.proyectoquantumkeyescrowsharding.domain.model.ShamirPqcKeyShardBundleToken;
import com.corp.proyectoquantumkeyescrowsharding.domain.port.out.ShamirPqcKeyShardBundleTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryShamirPqcKeyShardBundleTokenRepositoryAdapter implements ShamirPqcKeyShardBundleTokenRepositoryPort {

    private final ConcurrentMap<String, ShamirPqcKeyShardBundleToken> storage = new ConcurrentHashMap<>();

    @Override
    public ShamirPqcKeyShardBundleToken save(ShamirPqcKeyShardBundleToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<ShamirPqcKeyShardBundleToken> findById(String id, String tenantId) {
        ShamirPqcKeyShardBundleToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
