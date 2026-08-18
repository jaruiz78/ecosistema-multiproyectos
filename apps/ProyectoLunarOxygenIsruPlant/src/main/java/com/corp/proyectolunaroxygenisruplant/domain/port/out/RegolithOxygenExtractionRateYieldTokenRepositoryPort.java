package com.corp.proyectolunaroxygenisruplant.domain.port.out;

import com.corp.proyectolunaroxygenisruplant.domain.model.RegolithOxygenExtractionRateYieldToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface RegolithOxygenExtractionRateYieldTokenRepositoryPort {
    RegolithOxygenExtractionRateYieldToken save(RegolithOxygenExtractionRateYieldToken entity);
    Optional<RegolithOxygenExtractionRateYieldToken> findById(String id, String tenantId);
}
