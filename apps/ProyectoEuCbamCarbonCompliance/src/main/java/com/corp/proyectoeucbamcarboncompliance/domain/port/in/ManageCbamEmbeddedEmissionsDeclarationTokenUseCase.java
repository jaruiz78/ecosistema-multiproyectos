package com.corp.proyectoeucbamcarboncompliance.domain.port.in;

import com.corp.proyectoeucbamcarboncompliance.domain.model.CbamEmbeddedEmissionsDeclarationToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCbamEmbeddedEmissionsDeclarationTokenUseCase {
    CbamEmbeddedEmissionsDeclarationToken createCbamEmbeddedEmissionsDeclarationToken(String tenantId, String title, double value);
    Optional<CbamEmbeddedEmissionsDeclarationToken> findCbamEmbeddedEmissionsDeclarationTokenById(String id, String tenantId);
    CbamEmbeddedEmissionsDeclarationToken processOptimization(String id, String tenantId);
}
