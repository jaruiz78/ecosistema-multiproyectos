package com.corp.proyectoorbitaldebrislaserdeflector.domain.port.out;

import com.corp.proyectoorbitaldebrislaserdeflector.domain.model.LaserAblationImpulseDeltaVToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface LaserAblationImpulseDeltaVTokenRepositoryPort {
    LaserAblationImpulseDeltaVToken save(LaserAblationImpulseDeltaVToken entity);
    Optional<LaserAblationImpulseDeltaVToken> findById(String id, String tenantId);
}
