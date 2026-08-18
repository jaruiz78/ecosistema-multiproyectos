package com.corp.proyectothresholdblsoraclenetwork.domain.port.in;

import com.corp.proyectothresholdblsoraclenetwork.domain.model.BlsAggregatedSignatureDataFeedToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBlsAggregatedSignatureDataFeedTokenUseCase {
    BlsAggregatedSignatureDataFeedToken createBlsAggregatedSignatureDataFeedToken(String tenantId, String title, double value);
    Optional<BlsAggregatedSignatureDataFeedToken> findBlsAggregatedSignatureDataFeedTokenById(String id, String tenantId);
    BlsAggregatedSignatureDataFeedToken processOptimization(String id, String tenantId);
}
