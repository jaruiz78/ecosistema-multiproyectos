package com.corp.proyectoopticalsatellitegroundstation.domain.port.out;

import com.corp.proyectoopticalsatellitegroundstation.domain.model.StrehlRatioWavefrontCorrectionNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface StrehlRatioWavefrontCorrectionNodeRepositoryPort {
    StrehlRatioWavefrontCorrectionNode save(StrehlRatioWavefrontCorrectionNode entity);
    Optional<StrehlRatioWavefrontCorrectionNode> findById(String id, String tenantId);
}
