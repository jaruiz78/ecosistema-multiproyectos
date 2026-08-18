package com.corp.proyectothermoelectricwasteheatharvester.application.service;

import com.corp.proyectothermoelectricwasteheatharvester.domain.model.SeebeckThermalGradientModuleNode;
import com.corp.proyectothermoelectricwasteheatharvester.domain.port.in.ManageSeebeckThermalGradientModuleNodeUseCase;
import com.corp.proyectothermoelectricwasteheatharvester.domain.port.out.SeebeckThermalGradientModuleNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SeebeckThermalGradientModuleNode.
 */
@Service
public class SeebeckThermalGradientModuleNodeApplicationService implements ManageSeebeckThermalGradientModuleNodeUseCase {

    private final SeebeckThermalGradientModuleNodeRepositoryPort repositoryPort;

    public SeebeckThermalGradientModuleNodeApplicationService(SeebeckThermalGradientModuleNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SeebeckThermalGradientModuleNode createSeebeckThermalGradientModuleNode(String tenantId, String title, double value) {
        SeebeckThermalGradientModuleNode entity = new SeebeckThermalGradientModuleNode(
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
    public Optional<SeebeckThermalGradientModuleNode> findSeebeckThermalGradientModuleNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SeebeckThermalGradientModuleNode processOptimization(String id, String tenantId) {
        SeebeckThermalGradientModuleNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SeebeckThermalGradientModuleNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
