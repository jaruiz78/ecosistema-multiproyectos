package com.corp.proyectoplantauxinrootmorphogenesis.application.service;

import com.corp.proyectoplantauxinrootmorphogenesis.domain.model.AuxinGradientMorphogenesisNode;
import com.corp.proyectoplantauxinrootmorphogenesis.domain.port.in.ManageAuxinGradientMorphogenesisNodeUseCase;
import com.corp.proyectoplantauxinrootmorphogenesis.domain.port.out.AuxinGradientMorphogenesisNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AuxinGradientMorphogenesisNode.
 */
@Service
public class AuxinGradientMorphogenesisNodeApplicationService implements ManageAuxinGradientMorphogenesisNodeUseCase {

    private final AuxinGradientMorphogenesisNodeRepositoryPort repositoryPort;

    public AuxinGradientMorphogenesisNodeApplicationService(AuxinGradientMorphogenesisNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AuxinGradientMorphogenesisNode createAuxinGradientMorphogenesisNode(String tenantId, String title, double value) {
        AuxinGradientMorphogenesisNode entity = new AuxinGradientMorphogenesisNode(
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
    public Optional<AuxinGradientMorphogenesisNode> findAuxinGradientMorphogenesisNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AuxinGradientMorphogenesisNode processOptimization(String id, String tenantId) {
        AuxinGradientMorphogenesisNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AuxinGradientMorphogenesisNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
