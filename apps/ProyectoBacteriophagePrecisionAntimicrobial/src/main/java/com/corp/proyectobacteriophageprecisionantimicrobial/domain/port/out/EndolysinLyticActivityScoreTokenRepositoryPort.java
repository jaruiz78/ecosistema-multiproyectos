package com.corp.proyectobacteriophageprecisionantimicrobial.domain.port.out;

import com.corp.proyectobacteriophageprecisionantimicrobial.domain.model.EndolysinLyticActivityScoreToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface EndolysinLyticActivityScoreTokenRepositoryPort {
    EndolysinLyticActivityScoreToken save(EndolysinLyticActivityScoreToken entity);
    Optional<EndolysinLyticActivityScoreToken> findById(String id, String tenantId);
}
