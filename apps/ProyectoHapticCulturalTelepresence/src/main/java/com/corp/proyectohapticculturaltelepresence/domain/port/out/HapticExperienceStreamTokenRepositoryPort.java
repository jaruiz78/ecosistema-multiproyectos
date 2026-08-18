package com.corp.proyectohapticculturaltelepresence.domain.port.out;

import com.corp.proyectohapticculturaltelepresence.domain.model.HapticExperienceStreamToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface HapticExperienceStreamTokenRepositoryPort {
    HapticExperienceStreamToken save(HapticExperienceStreamToken entity);
    Optional<HapticExperienceStreamToken> findById(String id, String tenantId);
}
