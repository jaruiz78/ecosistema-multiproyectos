package com.corp.proyectoneuralmodelipprotection.domain.port.out;

import com.corp.proyectoneuralmodelipprotection.domain.model.ModelTensorWatermarkSignatureToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ModelTensorWatermarkSignatureTokenRepositoryPort {
    ModelTensorWatermarkSignatureToken save(ModelTensorWatermarkSignatureToken entity);
    Optional<ModelTensorWatermarkSignatureToken> findById(String id, String tenantId);
}
