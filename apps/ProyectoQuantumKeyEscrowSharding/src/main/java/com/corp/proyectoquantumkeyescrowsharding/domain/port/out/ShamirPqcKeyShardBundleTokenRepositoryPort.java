package com.corp.proyectoquantumkeyescrowsharding.domain.port.out;

import com.corp.proyectoquantumkeyescrowsharding.domain.model.ShamirPqcKeyShardBundleToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ShamirPqcKeyShardBundleTokenRepositoryPort {
    ShamirPqcKeyShardBundleToken save(ShamirPqcKeyShardBundleToken entity);
    Optional<ShamirPqcKeyShardBundleToken> findById(String id, String tenantId);
}
