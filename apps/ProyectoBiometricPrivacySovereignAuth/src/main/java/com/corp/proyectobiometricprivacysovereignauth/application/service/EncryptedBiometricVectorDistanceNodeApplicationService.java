package com.corp.proyectobiometricprivacysovereignauth.application.service;

import com.corp.proyectobiometricprivacysovereignauth.domain.model.EncryptedBiometricVectorDistanceNode;
import com.corp.proyectobiometricprivacysovereignauth.domain.port.in.ManageEncryptedBiometricVectorDistanceNodeUseCase;
import com.corp.proyectobiometricprivacysovereignauth.domain.port.out.EncryptedBiometricVectorDistanceNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EncryptedBiometricVectorDistanceNode.
 */
@Service
public class EncryptedBiometricVectorDistanceNodeApplicationService implements ManageEncryptedBiometricVectorDistanceNodeUseCase {

    private final EncryptedBiometricVectorDistanceNodeRepositoryPort repositoryPort;

    public EncryptedBiometricVectorDistanceNodeApplicationService(EncryptedBiometricVectorDistanceNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EncryptedBiometricVectorDistanceNode createEncryptedBiometricVectorDistanceNode(String tenantId, String title, double value) {
        EncryptedBiometricVectorDistanceNode entity = new EncryptedBiometricVectorDistanceNode(
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
    public Optional<EncryptedBiometricVectorDistanceNode> findEncryptedBiometricVectorDistanceNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EncryptedBiometricVectorDistanceNode processOptimization(String id, String tenantId) {
        EncryptedBiometricVectorDistanceNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EncryptedBiometricVectorDistanceNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
