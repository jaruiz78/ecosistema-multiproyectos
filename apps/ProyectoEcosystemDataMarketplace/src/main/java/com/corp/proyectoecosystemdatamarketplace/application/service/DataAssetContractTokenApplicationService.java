package com.corp.proyectoecosystemdatamarketplace.application.service;

import com.corp.proyectoecosystemdatamarketplace.domain.model.DataAssetContractToken;
import com.corp.proyectoecosystemdatamarketplace.domain.port.in.ManageDataAssetContractTokenUseCase;
import com.corp.proyectoecosystemdatamarketplace.domain.port.out.DataAssetContractTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DataAssetContractToken.
 */
@Service
public class DataAssetContractTokenApplicationService implements ManageDataAssetContractTokenUseCase {

    private final DataAssetContractTokenRepositoryPort repositoryPort;

    public DataAssetContractTokenApplicationService(DataAssetContractTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DataAssetContractToken createDataAssetContractToken(String tenantId, String title, double value) {
        DataAssetContractToken entity = new DataAssetContractToken(
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
    public Optional<DataAssetContractToken> findDataAssetContractTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DataAssetContractToken processOptimization(String id, String tenantId) {
        DataAssetContractToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DataAssetContractToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
