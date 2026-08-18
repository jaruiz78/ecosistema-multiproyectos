package com.corp.proyectoneuralmodelipprotection.application.service;

import com.corp.proyectoneuralmodelipprotection.domain.model.ModelTensorWatermarkSignatureToken;
import com.corp.proyectoneuralmodelipprotection.domain.port.in.ManageModelTensorWatermarkSignatureTokenUseCase;
import com.corp.proyectoneuralmodelipprotection.domain.port.out.ModelTensorWatermarkSignatureTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ModelTensorWatermarkSignatureToken.
 */
@Service
public class ModelTensorWatermarkSignatureTokenApplicationService implements ManageModelTensorWatermarkSignatureTokenUseCase {

    private final ModelTensorWatermarkSignatureTokenRepositoryPort repositoryPort;

    public ModelTensorWatermarkSignatureTokenApplicationService(ModelTensorWatermarkSignatureTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ModelTensorWatermarkSignatureToken createModelTensorWatermarkSignatureToken(String tenantId, String title, double value) {
        ModelTensorWatermarkSignatureToken entity = new ModelTensorWatermarkSignatureToken(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<ModelTensorWatermarkSignatureToken> findModelTensorWatermarkSignatureTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ModelTensorWatermarkSignatureToken processOptimization(String id, String tenantId) {
        ModelTensorWatermarkSignatureToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ModelTensorWatermarkSignatureToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
