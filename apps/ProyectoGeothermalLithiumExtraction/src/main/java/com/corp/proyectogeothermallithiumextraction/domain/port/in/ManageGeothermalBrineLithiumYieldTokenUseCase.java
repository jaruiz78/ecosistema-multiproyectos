package com.corp.proyectogeothermallithiumextraction.domain.port.in;

import com.corp.proyectogeothermallithiumextraction.domain.model.GeothermalBrineLithiumYieldToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGeothermalBrineLithiumYieldTokenUseCase {
    GeothermalBrineLithiumYieldToken createGeothermalBrineLithiumYieldToken(String tenantId, String title, double value);
    Optional<GeothermalBrineLithiumYieldToken> findGeothermalBrineLithiumYieldTokenById(String id, String tenantId);
    GeothermalBrineLithiumYieldToken processOptimization(String id, String tenantId);
}
