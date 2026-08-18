package com.corp.proyectofemtosecondlaserprecision.domain.port.in;

import com.corp.proyectofemtosecondlaserprecision.domain.model.LaserAblationPulseProfileToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageLaserAblationPulseProfileTokenUseCase {
    LaserAblationPulseProfileToken createLaserAblationPulseProfileToken(String tenantId, String title, double value);
    Optional<LaserAblationPulseProfileToken> findLaserAblationPulseProfileTokenById(String id, String tenantId);
    LaserAblationPulseProfileToken processOptimization(String id, String tenantId);
}
