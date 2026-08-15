package com.corp.proyectobiotecnologia.application.service;

import com.corp.proyectobiotecnologia.domain.model.BioCompound;
import com.corp.proyectobiotecnologia.domain.port.in.ManageBioCompoundUseCase;
import com.corp.proyectobiotecnologia.domain.port.out.BioCompoundRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BioCompound.
 */
@Service
public class BioCompoundApplicationService implements ManageBioCompoundUseCase {

    private final BioCompoundRepositoryPort repositoryPort;

    public BioCompoundApplicationService(BioCompoundRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BioCompound createBioCompound(String tenantId, String title, double value) {
        BioCompound entity = new BioCompound(
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
    public Optional<BioCompound> findBioCompoundById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BioCompound processOptimization(String id, String tenantId) {
        BioCompound existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BioCompound optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
