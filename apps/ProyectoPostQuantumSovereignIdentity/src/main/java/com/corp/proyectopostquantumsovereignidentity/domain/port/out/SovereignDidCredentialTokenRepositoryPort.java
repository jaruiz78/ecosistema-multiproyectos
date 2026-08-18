package com.corp.proyectopostquantumsovereignidentity.domain.port.out;

import com.corp.proyectopostquantumsovereignidentity.domain.model.SovereignDidCredentialToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface SovereignDidCredentialTokenRepositoryPort {
    SovereignDidCredentialToken save(SovereignDidCredentialToken entity);
    Optional<SovereignDidCredentialToken> findById(String id, String tenantId);
}
