package com.corp.proyectothresholdblsoraclenetwork.domain.port.out;

import com.corp.proyectothresholdblsoraclenetwork.domain.model.BlsAggregatedSignatureDataFeedToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BlsAggregatedSignatureDataFeedTokenRepositoryPort {
    BlsAggregatedSignatureDataFeedToken save(BlsAggregatedSignatureDataFeedToken entity);
    Optional<BlsAggregatedSignatureDataFeedToken> findById(String id, String tenantId);
}
