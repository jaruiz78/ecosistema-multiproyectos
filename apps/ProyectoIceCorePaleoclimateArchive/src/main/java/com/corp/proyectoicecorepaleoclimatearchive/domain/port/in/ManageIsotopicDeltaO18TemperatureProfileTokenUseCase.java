package com.corp.proyectoicecorepaleoclimatearchive.domain.port.in;

import com.corp.proyectoicecorepaleoclimatearchive.domain.model.IsotopicDeltaO18TemperatureProfileToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageIsotopicDeltaO18TemperatureProfileTokenUseCase {
    IsotopicDeltaO18TemperatureProfileToken createIsotopicDeltaO18TemperatureProfileToken(String tenantId, String title, double value);
    Optional<IsotopicDeltaO18TemperatureProfileToken> findIsotopicDeltaO18TemperatureProfileTokenById(String id, String tenantId);
    IsotopicDeltaO18TemperatureProfileToken processOptimization(String id, String tenantId);
}
