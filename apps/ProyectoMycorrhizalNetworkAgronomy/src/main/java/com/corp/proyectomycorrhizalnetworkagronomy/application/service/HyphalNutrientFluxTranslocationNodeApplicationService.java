package com.corp.proyectomycorrhizalnetworkagronomy.application.service;

import com.corp.proyectomycorrhizalnetworkagronomy.domain.model.HyphalNutrientFluxTranslocationNode;
import com.corp.proyectomycorrhizalnetworkagronomy.domain.port.in.ManageHyphalNutrientFluxTranslocationNodeUseCase;
import com.corp.proyectomycorrhizalnetworkagronomy.domain.port.out.HyphalNutrientFluxTranslocationNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HyphalNutrientFluxTranslocationNode.
 */
@Service
public class HyphalNutrientFluxTranslocationNodeApplicationService implements ManageHyphalNutrientFluxTranslocationNodeUseCase {

    private final HyphalNutrientFluxTranslocationNodeRepositoryPort repositoryPort;

    public HyphalNutrientFluxTranslocationNodeApplicationService(HyphalNutrientFluxTranslocationNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HyphalNutrientFluxTranslocationNode createHyphalNutrientFluxTranslocationNode(String tenantId, String title, double value) {
        HyphalNutrientFluxTranslocationNode entity = new HyphalNutrientFluxTranslocationNode(
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
    public Optional<HyphalNutrientFluxTranslocationNode> findHyphalNutrientFluxTranslocationNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HyphalNutrientFluxTranslocationNode processOptimization(String id, String tenantId) {
        HyphalNutrientFluxTranslocationNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HyphalNutrientFluxTranslocationNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
