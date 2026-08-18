package com.corp.proyectoconfidentialdatacleanroom.domain.port.out;

import com.corp.proyectoconfidentialdatacleanroom.domain.model.SecureEnclaveAnalyticsAttestationToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SecureEnclaveAnalyticsAttestationTokenRepositoryPort {
    SecureEnclaveAnalyticsAttestationToken save(SecureEnclaveAnalyticsAttestationToken entity);
    Optional<SecureEnclaveAnalyticsAttestationToken> findById(String id, String tenantId);
}
