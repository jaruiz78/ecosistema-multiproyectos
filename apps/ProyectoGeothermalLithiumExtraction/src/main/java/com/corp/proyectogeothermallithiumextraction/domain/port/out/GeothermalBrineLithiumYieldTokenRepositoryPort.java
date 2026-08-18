package com.corp.proyectogeothermallithiumextraction.domain.port.out;

import com.corp.proyectogeothermallithiumextraction.domain.model.GeothermalBrineLithiumYieldToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GeothermalBrineLithiumYieldTokenRepositoryPort {
    GeothermalBrineLithiumYieldToken save(GeothermalBrineLithiumYieldToken entity);
    Optional<GeothermalBrineLithiumYieldToken> findById(String id, String tenantId);
}
