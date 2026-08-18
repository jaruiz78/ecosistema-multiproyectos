package com.corp.proyectoecosystemdatamarketplace.infrastructure.adapter.out.persistence;

import com.corp.proyectoecosystemdatamarketplace.domain.model.DataAssetContractToken;
import com.corp.proyectoecosystemdatamarketplace.domain.port.out.DataAssetContractTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryDataAssetContractTokenRepositoryAdapter implements DataAssetContractTokenRepositoryPort {

    private final ConcurrentMap<String, DataAssetContractToken> storage = new ConcurrentHashMap<>();

    @Override
    public DataAssetContractToken save(DataAssetContractToken entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<DataAssetContractToken> findById(String id, String tenantId) {
        DataAssetContractToken entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
