package com.corp.proyectoalgalbiodieselrefinery.domain.port.out;

import com.corp.proyectoalgalbiodieselrefinery.domain.model.LipidToBiodieselYieldConversionToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface LipidToBiodieselYieldConversionTokenRepositoryPort {
    LipidToBiodieselYieldConversionToken save(LipidToBiodieselYieldConversionToken entity);
    Optional<LipidToBiodieselYieldConversionToken> findById(String id, String tenantId);
}
