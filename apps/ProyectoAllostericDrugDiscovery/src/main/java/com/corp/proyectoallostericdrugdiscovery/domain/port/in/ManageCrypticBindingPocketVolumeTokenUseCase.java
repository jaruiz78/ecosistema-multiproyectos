package com.corp.proyectoallostericdrugdiscovery.domain.port.in;

import com.corp.proyectoallostericdrugdiscovery.domain.model.CrypticBindingPocketVolumeToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCrypticBindingPocketVolumeTokenUseCase {
    CrypticBindingPocketVolumeToken createCrypticBindingPocketVolumeToken(String tenantId, String title, double value);
    Optional<CrypticBindingPocketVolumeToken> findCrypticBindingPocketVolumeTokenById(String id, String tenantId);
    CrypticBindingPocketVolumeToken processOptimization(String id, String tenantId);
}
