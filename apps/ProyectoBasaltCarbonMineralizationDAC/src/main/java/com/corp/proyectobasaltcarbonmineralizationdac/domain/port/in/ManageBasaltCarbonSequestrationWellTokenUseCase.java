package com.corp.proyectobasaltcarbonmineralizationdac.domain.port.in;

import com.corp.proyectobasaltcarbonmineralizationdac.domain.model.BasaltCarbonSequestrationWellToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBasaltCarbonSequestrationWellTokenUseCase {
    BasaltCarbonSequestrationWellToken createBasaltCarbonSequestrationWellToken(String tenantId, String title, double value);
    Optional<BasaltCarbonSequestrationWellToken> findBasaltCarbonSequestrationWellTokenById(String id, String tenantId);
    BasaltCarbonSequestrationWellToken processOptimization(String id, String tenantId);
}
