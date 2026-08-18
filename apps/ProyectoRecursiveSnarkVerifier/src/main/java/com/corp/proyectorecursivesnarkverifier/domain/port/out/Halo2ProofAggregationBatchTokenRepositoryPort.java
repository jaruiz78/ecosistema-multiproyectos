package com.corp.proyectorecursivesnarkverifier.domain.port.out;

import com.corp.proyectorecursivesnarkverifier.domain.model.Halo2ProofAggregationBatchToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface Halo2ProofAggregationBatchTokenRepositoryPort {
    Halo2ProofAggregationBatchToken save(Halo2ProofAggregationBatchToken entity);
    Optional<Halo2ProofAggregationBatchToken> findById(String id, String tenantId);
}
