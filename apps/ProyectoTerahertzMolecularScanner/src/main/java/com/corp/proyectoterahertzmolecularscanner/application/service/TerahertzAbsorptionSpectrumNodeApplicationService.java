package com.corp.proyectoterahertzmolecularscanner.application.service;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import com.corp.proyectoterahertzmolecularscanner.domain.port.in.ManageTerahertzAbsorptionSpectrumNodeUseCase;
import com.corp.proyectoterahertzmolecularscanner.domain.port.out.TerahertzAbsorptionSpectrumNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de TerahertzAbsorptionSpectrumNode.
 */
@Service
public class TerahertzAbsorptionSpectrumNodeApplicationService implements ManageTerahertzAbsorptionSpectrumNodeUseCase {

    private final TerahertzAbsorptionSpectrumNodeRepositoryPort repositoryPort;

    public TerahertzAbsorptionSpectrumNodeApplicationService(TerahertzAbsorptionSpectrumNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TerahertzAbsorptionSpectrumNode createTerahertzAbsorptionSpectrumNode(String tenantId, String title, double value) {
        TerahertzAbsorptionSpectrumNode entity = new TerahertzAbsorptionSpectrumNode(
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
    public Optional<TerahertzAbsorptionSpectrumNode> findTerahertzAbsorptionSpectrumNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TerahertzAbsorptionSpectrumNode processOptimization(String id, String tenantId) {
        TerahertzAbsorptionSpectrumNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TerahertzAbsorptionSpectrumNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
