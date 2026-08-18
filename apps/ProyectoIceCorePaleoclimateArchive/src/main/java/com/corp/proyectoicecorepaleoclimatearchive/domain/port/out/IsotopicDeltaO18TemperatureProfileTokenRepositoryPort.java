package com.corp.proyectoicecorepaleoclimatearchive.domain.port.out;

import com.corp.proyectoicecorepaleoclimatearchive.domain.model.IsotopicDeltaO18TemperatureProfileToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface IsotopicDeltaO18TemperatureProfileTokenRepositoryPort {
    IsotopicDeltaO18TemperatureProfileToken save(IsotopicDeltaO18TemperatureProfileToken entity);
    Optional<IsotopicDeltaO18TemperatureProfileToken> findById(String id, String tenantId);
}
