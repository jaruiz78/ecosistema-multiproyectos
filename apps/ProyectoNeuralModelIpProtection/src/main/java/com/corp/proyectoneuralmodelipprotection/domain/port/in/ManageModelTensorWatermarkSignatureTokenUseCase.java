package com.corp.proyectoneuralmodelipprotection.domain.port.in;

import com.corp.proyectoneuralmodelipprotection.domain.model.ModelTensorWatermarkSignatureToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageModelTensorWatermarkSignatureTokenUseCase {
    ModelTensorWatermarkSignatureToken createModelTensorWatermarkSignatureToken(String tenantId, String title, double value);
    Optional<ModelTensorWatermarkSignatureToken> findModelTensorWatermarkSignatureTokenById(String id, String tenantId);
    ModelTensorWatermarkSignatureToken processOptimization(String id, String tenantId);
}
