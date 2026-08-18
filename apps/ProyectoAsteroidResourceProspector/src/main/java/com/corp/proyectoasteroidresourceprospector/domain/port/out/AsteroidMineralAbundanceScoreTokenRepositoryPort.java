package com.corp.proyectoasteroidresourceprospector.domain.port.out;

import com.corp.proyectoasteroidresourceprospector.domain.model.AsteroidMineralAbundanceScoreToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AsteroidMineralAbundanceScoreTokenRepositoryPort {
    AsteroidMineralAbundanceScoreToken save(AsteroidMineralAbundanceScoreToken entity);
    Optional<AsteroidMineralAbundanceScoreToken> findById(String id, String tenantId);
}
