package com.corp.proyectoeucbamcarboncompliance.domain.port.out;

import com.corp.proyectoeucbamcarboncompliance.domain.model.CbamEmbeddedEmissionsDeclarationToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CbamEmbeddedEmissionsDeclarationTokenRepositoryPort {
    CbamEmbeddedEmissionsDeclarationToken save(CbamEmbeddedEmissionsDeclarationToken entity);
    Optional<CbamEmbeddedEmissionsDeclarationToken> findById(String id, String tenantId);
}
