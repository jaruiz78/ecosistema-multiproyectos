package com.corp.proyectohapticculturaltelepresence.domain.port.in;

import com.corp.proyectohapticculturaltelepresence.domain.model.HapticExperienceStreamToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHapticExperienceStreamTokenUseCase {
    HapticExperienceStreamToken createHapticExperienceStreamToken(String tenantId, String title, double value);
    Optional<HapticExperienceStreamToken> findHapticExperienceStreamTokenById(String id, String tenantId);
    HapticExperienceStreamToken processOptimization(String id, String tenantId);
}
