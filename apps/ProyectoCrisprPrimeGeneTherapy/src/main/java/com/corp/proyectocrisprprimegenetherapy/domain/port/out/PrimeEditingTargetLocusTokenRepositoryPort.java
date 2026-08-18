package com.corp.proyectocrisprprimegenetherapy.domain.port.out;

import com.corp.proyectocrisprprimegenetherapy.domain.model.PrimeEditingTargetLocusToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PrimeEditingTargetLocusTokenRepositoryPort {
    PrimeEditingTargetLocusToken save(PrimeEditingTargetLocusToken entity);
    Optional<PrimeEditingTargetLocusToken> findById(String id, String tenantId);
}
