package com.corp.proyectoeidas2digitalidentitywallet.domain.port.in;

import com.corp.proyectoeidas2digitalidentitywallet.domain.model.VerifiableCredentialStatusListToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageVerifiableCredentialStatusListTokenUseCase {
    VerifiableCredentialStatusListToken createVerifiableCredentialStatusListToken(String tenantId, String title, double value);
    Optional<VerifiableCredentialStatusListToken> findVerifiableCredentialStatusListTokenById(String id, String tenantId);
    VerifiableCredentialStatusListToken processOptimization(String id, String tenantId);
}
