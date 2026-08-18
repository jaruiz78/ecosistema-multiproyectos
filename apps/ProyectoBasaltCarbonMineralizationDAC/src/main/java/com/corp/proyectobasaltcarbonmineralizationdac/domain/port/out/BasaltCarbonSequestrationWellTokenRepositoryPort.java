package com.corp.proyectobasaltcarbonmineralizationdac.domain.port.out;

import com.corp.proyectobasaltcarbonmineralizationdac.domain.model.BasaltCarbonSequestrationWellToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BasaltCarbonSequestrationWellTokenRepositoryPort {
    BasaltCarbonSequestrationWellToken save(BasaltCarbonSequestrationWellToken entity);
    Optional<BasaltCarbonSequestrationWellToken> findById(String id, String tenantId);
}
