package com.corp.proyectoecosystemdatamarketplace.domain.port.out;

import com.corp.proyectoecosystemdatamarketplace.domain.model.DataAssetContractToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface DataAssetContractTokenRepositoryPort {
    DataAssetContractToken save(DataAssetContractToken entity);
    Optional<DataAssetContractToken> findById(String id, String tenantId);
}
