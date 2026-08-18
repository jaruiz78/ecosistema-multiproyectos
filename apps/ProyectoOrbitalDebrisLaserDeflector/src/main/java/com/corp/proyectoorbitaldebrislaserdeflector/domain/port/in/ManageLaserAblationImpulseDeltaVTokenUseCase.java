package com.corp.proyectoorbitaldebrislaserdeflector.domain.port.in;

import com.corp.proyectoorbitaldebrislaserdeflector.domain.model.LaserAblationImpulseDeltaVToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageLaserAblationImpulseDeltaVTokenUseCase {
    LaserAblationImpulseDeltaVToken createLaserAblationImpulseDeltaVToken(String tenantId, String title, double value);
    Optional<LaserAblationImpulseDeltaVToken> findLaserAblationImpulseDeltaVTokenById(String id, String tenantId);
    LaserAblationImpulseDeltaVToken processOptimization(String id, String tenantId);
}
