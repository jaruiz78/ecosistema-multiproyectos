package com.corp.proyectostratosphericsai.domain.port.in;

import com.corp.proyectostratosphericsai.domain.model.AerosolInjectionPlume;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAerosolInjectionPlumeUseCase {
    AerosolInjectionPlume createAerosolInjectionPlume(String tenantId, String title, double value);
    Optional<AerosolInjectionPlume> findAerosolInjectionPlumeById(String id, String tenantId);
    AerosolInjectionPlume processOptimization(String id, String tenantId);
}
