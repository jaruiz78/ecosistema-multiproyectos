package com.corp.proyectointerplanetarydelaytolerantrelay.application.service;

import com.corp.proyectointerplanetarydelaytolerantrelay.domain.model.DtnBundleCustodyTransferToken;
import com.corp.proyectointerplanetarydelaytolerantrelay.domain.port.in.ManageDtnBundleCustodyTransferTokenUseCase;
import com.corp.proyectointerplanetarydelaytolerantrelay.domain.port.out.DtnBundleCustodyTransferTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DtnBundleCustodyTransferToken.
 */
@Service
public class DtnBundleCustodyTransferTokenApplicationService implements ManageDtnBundleCustodyTransferTokenUseCase {

    private final DtnBundleCustodyTransferTokenRepositoryPort repositoryPort;

    public DtnBundleCustodyTransferTokenApplicationService(DtnBundleCustodyTransferTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DtnBundleCustodyTransferToken createDtnBundleCustodyTransferToken(String tenantId, String title, double value) {
        DtnBundleCustodyTransferToken entity = new DtnBundleCustodyTransferToken(
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
    public Optional<DtnBundleCustodyTransferToken> findDtnBundleCustodyTransferTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DtnBundleCustodyTransferToken processOptimization(String id, String tenantId) {
        DtnBundleCustodyTransferToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DtnBundleCustodyTransferToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
