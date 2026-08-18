package com.corp.proyectoeidas2digitalidentitywallet.domain.port.out;

import com.corp.proyectoeidas2digitalidentitywallet.domain.model.VerifiableCredentialStatusListToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface VerifiableCredentialStatusListTokenRepositoryPort {
    VerifiableCredentialStatusListToken save(VerifiableCredentialStatusListToken entity);
    Optional<VerifiableCredentialStatusListToken> findById(String id, String tenantId);
}
