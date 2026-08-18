package com.corp.proyectoecosystemdatamarketplace.domain.port.in;

import com.corp.proyectoecosystemdatamarketplace.domain.model.DataAssetContractToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageDataAssetContractTokenUseCase {
    DataAssetContractToken createDataAssetContractToken(String tenantId, String title, double value);
    Optional<DataAssetContractToken> findDataAssetContractTokenById(String id, String tenantId);
    DataAssetContractToken processOptimization(String id, String tenantId);
}
