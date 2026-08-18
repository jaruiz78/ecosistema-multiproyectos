package com.corp.proyectoallostericdrugdiscovery.domain.port.out;

import com.corp.proyectoallostericdrugdiscovery.domain.model.CrypticBindingPocketVolumeToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CrypticBindingPocketVolumeTokenRepositoryPort {
    CrypticBindingPocketVolumeToken save(CrypticBindingPocketVolumeToken entity);
    Optional<CrypticBindingPocketVolumeToken> findById(String id, String tenantId);
}
