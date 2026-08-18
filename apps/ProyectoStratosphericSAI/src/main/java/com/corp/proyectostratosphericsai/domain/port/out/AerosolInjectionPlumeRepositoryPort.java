package com.corp.proyectostratosphericsai.domain.port.out;

import com.corp.proyectostratosphericsai.domain.model.AerosolInjectionPlume;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AerosolInjectionPlumeRepositoryPort {
    AerosolInjectionPlume save(AerosolInjectionPlume entity);
    Optional<AerosolInjectionPlume> findById(String id, String tenantId);
}
