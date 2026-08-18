package com.corp.proyectoalgalbiodieselrefinery.domain.port.in;

import com.corp.proyectoalgalbiodieselrefinery.domain.model.LipidToBiodieselYieldConversionToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageLipidToBiodieselYieldConversionTokenUseCase {
    LipidToBiodieselYieldConversionToken createLipidToBiodieselYieldConversionToken(String tenantId, String title, double value);
    Optional<LipidToBiodieselYieldConversionToken> findLipidToBiodieselYieldConversionTokenById(String id, String tenantId);
    LipidToBiodieselYieldConversionToken processOptimization(String id, String tenantId);
}
