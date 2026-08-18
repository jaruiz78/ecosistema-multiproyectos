package com.corp.proyectoconfidentialdatacleanroom.domain.port.in;

import com.corp.proyectoconfidentialdatacleanroom.domain.model.SecureEnclaveAnalyticsAttestationToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageSecureEnclaveAnalyticsAttestationTokenUseCase {
    SecureEnclaveAnalyticsAttestationToken createSecureEnclaveAnalyticsAttestationToken(String tenantId, String title, double value);
    Optional<SecureEnclaveAnalyticsAttestationToken> findSecureEnclaveAnalyticsAttestationTokenById(String id, String tenantId);
    SecureEnclaveAnalyticsAttestationToken processOptimization(String id, String tenantId);
}
