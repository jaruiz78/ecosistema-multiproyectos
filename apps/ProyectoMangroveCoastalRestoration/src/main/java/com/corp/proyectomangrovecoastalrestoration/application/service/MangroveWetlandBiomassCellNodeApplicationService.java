package com.corp.proyectomangrovecoastalrestoration.application.service;

import com.corp.proyectomangrovecoastalrestoration.domain.model.MangroveWetlandBiomassCellNode;
import com.corp.proyectomangrovecoastalrestoration.domain.port.in.ManageMangroveWetlandBiomassCellNodeUseCase;
import com.corp.proyectomangrovecoastalrestoration.domain.port.out.MangroveWetlandBiomassCellNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MangroveWetlandBiomassCellNode.
 */
@Service
public class MangroveWetlandBiomassCellNodeApplicationService implements ManageMangroveWetlandBiomassCellNodeUseCase {

    private final MangroveWetlandBiomassCellNodeRepositoryPort repositoryPort;

    public MangroveWetlandBiomassCellNodeApplicationService(MangroveWetlandBiomassCellNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MangroveWetlandBiomassCellNode createMangroveWetlandBiomassCellNode(String tenantId, String title, double value) {
        MangroveWetlandBiomassCellNode entity = new MangroveWetlandBiomassCellNode(
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
    public Optional<MangroveWetlandBiomassCellNode> findMangroveWetlandBiomassCellNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MangroveWetlandBiomassCellNode processOptimization(String id, String tenantId) {
        MangroveWetlandBiomassCellNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MangroveWetlandBiomassCellNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
