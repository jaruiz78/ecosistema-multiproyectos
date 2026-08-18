package com.corp.proyectorecursivesnarkverifier.domain.port.in;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageHalo2ProofAggregationBatchTokenUseCase {
    Halo2ProofAggregationBatchToken createHalo2ProofAggregationBatchToken(String tenantId, String title, double value);
    Optional<Halo2ProofAggregationBatchToken> findHalo2ProofAggregationBatchTokenById(String id, String tenantId);
    Halo2ProofAggregationBatchToken processOptimization(String id, String tenantId);
}
