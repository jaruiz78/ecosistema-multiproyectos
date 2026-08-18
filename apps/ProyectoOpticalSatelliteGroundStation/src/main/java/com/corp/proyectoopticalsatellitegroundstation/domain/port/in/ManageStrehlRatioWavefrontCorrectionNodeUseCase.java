package com.corp.proyectoopticalsatellitegroundstation.domain.port.in;

import com.corp.proyectoopticalsatellitegroundstation.domain.model.StrehlRatioWavefrontCorrectionNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageStrehlRatioWavefrontCorrectionNodeUseCase {
    StrehlRatioWavefrontCorrectionNode createStrehlRatioWavefrontCorrectionNode(String tenantId, String title, double value);
    Optional<StrehlRatioWavefrontCorrectionNode> findStrehlRatioWavefrontCorrectionNodeById(String id, String tenantId);
    StrehlRatioWavefrontCorrectionNode processOptimization(String id, String tenantId);
}
