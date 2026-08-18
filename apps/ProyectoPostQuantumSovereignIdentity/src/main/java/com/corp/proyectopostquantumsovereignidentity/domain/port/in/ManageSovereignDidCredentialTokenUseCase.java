package com.corp.proyectopostquantumsovereignidentity.domain.port.in;

import com.corp.proyectopostquantumsovereignidentity.domain.model.SovereignDidCredentialToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSovereignDidCredentialTokenUseCase {
    SovereignDidCredentialToken createSovereignDidCredentialToken(String tenantId, String title, double value);
    Optional<SovereignDidCredentialToken> findSovereignDidCredentialTokenById(String id, String tenantId);
    SovereignDidCredentialToken processOptimization(String id, String tenantId);
}
