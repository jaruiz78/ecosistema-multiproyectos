package com.corp.proyectoquantumkeyescrowsharding.domain.port.in;

import com.corp.proyectoquantumkeyescrowsharding.domain.model.ShamirPqcKeyShardBundleToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageShamirPqcKeyShardBundleTokenUseCase {
    ShamirPqcKeyShardBundleToken createShamirPqcKeyShardBundleToken(String tenantId, String title, double value);
    Optional<ShamirPqcKeyShardBundleToken> findShamirPqcKeyShardBundleTokenById(String id, String tenantId);
    ShamirPqcKeyShardBundleToken processOptimization(String id, String tenantId);
}
