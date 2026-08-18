package com.corp.proyectoasteroidresourceprospector.domain.port.in;

import com.corp.proyectoasteroidresourceprospector.domain.model.AsteroidMineralAbundanceScoreToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAsteroidMineralAbundanceScoreTokenUseCase {
    AsteroidMineralAbundanceScoreToken createAsteroidMineralAbundanceScoreToken(String tenantId, String title, double value);
    Optional<AsteroidMineralAbundanceScoreToken> findAsteroidMineralAbundanceScoreTokenById(String id, String tenantId);
    AsteroidMineralAbundanceScoreToken processOptimization(String id, String tenantId);
}
