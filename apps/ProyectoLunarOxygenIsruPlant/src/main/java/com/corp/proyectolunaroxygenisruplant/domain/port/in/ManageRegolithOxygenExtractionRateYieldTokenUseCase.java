package com.corp.proyectolunaroxygenisruplant.domain.port.in;

import com.corp.proyectolunaroxygenisruplant.domain.model.RegolithOxygenExtractionRateYieldToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageRegolithOxygenExtractionRateYieldTokenUseCase {
    RegolithOxygenExtractionRateYieldToken createRegolithOxygenExtractionRateYieldToken(String tenantId, String title, double value);
    Optional<RegolithOxygenExtractionRateYieldToken> findRegolithOxygenExtractionRateYieldTokenById(String id, String tenantId);
    RegolithOxygenExtractionRateYieldToken processOptimization(String id, String tenantId);
}
