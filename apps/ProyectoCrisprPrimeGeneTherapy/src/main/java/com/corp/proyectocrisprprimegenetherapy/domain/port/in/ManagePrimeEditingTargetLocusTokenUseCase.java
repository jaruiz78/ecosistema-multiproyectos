package com.corp.proyectocrisprprimegenetherapy.domain.port.in;

import com.corp.proyectocrisprprimegenetherapy.domain.model.PrimeEditingTargetLocusToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePrimeEditingTargetLocusTokenUseCase {
    PrimeEditingTargetLocusToken createPrimeEditingTargetLocusToken(String tenantId, String title, double value);
    Optional<PrimeEditingTargetLocusToken> findPrimeEditingTargetLocusTokenById(String id, String tenantId);
    PrimeEditingTargetLocusToken processOptimization(String id, String tenantId);
}
