package com.corp.proyectobacteriophageprecisionantimicrobial.domain.port.in;

import com.corp.proyectobacteriophageprecisionantimicrobial.domain.model.EndolysinLyticActivityScoreToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageEndolysinLyticActivityScoreTokenUseCase {
    EndolysinLyticActivityScoreToken createEndolysinLyticActivityScoreToken(String tenantId, String title, double value);
    Optional<EndolysinLyticActivityScoreToken> findEndolysinLyticActivityScoreTokenById(String id, String tenantId);
    EndolysinLyticActivityScoreToken processOptimization(String id, String tenantId);
}
