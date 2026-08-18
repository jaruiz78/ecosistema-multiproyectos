package com.corp.proyectofemtosecondlaserprecision.domain.port.out;

import com.corp.proyectofemtosecondlaserprecision.domain.model.LaserAblationPulseProfileToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface LaserAblationPulseProfileTokenRepositoryPort {
    LaserAblationPulseProfileToken save(LaserAblationPulseProfileToken entity);
    Optional<LaserAblationPulseProfileToken> findById(String id, String tenantId);
}
