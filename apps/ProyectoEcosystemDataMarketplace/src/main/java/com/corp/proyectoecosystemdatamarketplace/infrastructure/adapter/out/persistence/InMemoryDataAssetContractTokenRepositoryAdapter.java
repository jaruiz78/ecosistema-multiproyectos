package com.corp.proyectoecosystemdatamarketplace.infrastructure.adapter.out.persistence;

import com.corp.proyectoecosystemdatamarketplace.domain.model.DataAssetContractToken;
import com.corp.proyectoecosystemdatamarketplace.domain.port.out.DataAssetContractTokenRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
