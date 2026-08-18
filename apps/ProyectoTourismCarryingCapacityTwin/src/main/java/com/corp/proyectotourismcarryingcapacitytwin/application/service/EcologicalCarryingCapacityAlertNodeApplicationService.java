package com.corp.proyectotourismcarryingcapacitytwin.application.service;

import com.corp.proyectotourismcarryingcapacitytwin.domain.model.EcologicalCarryingCapacityAlertNode;
import com.corp.proyectotourismcarryingcapacitytwin.domain.port.in.ManageEcologicalCarryingCapacityAlertNodeUseCase;
import com.corp.proyectotourismcarryingcapacitytwin.domain.port.out.EcologicalCarryingCapacityAlertNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EcologicalCarryingCapacityAlertNode.
 */
@Service
public class EcologicalCarryingCapacityAlertNodeApplicationService implements ManageEcologicalCarryingCapacityAlertNodeUseCase {

    private final EcologicalCarryingCapacityAlertNodeRepositoryPort repositoryPort;

    public EcologicalCarryingCapacityAlertNodeApplicationService(EcologicalCarryingCapacityAlertNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EcologicalCarryingCapacityAlertNode createEcologicalCarryingCapacityAlertNode(String tenantId, String title, double value) {
        EcologicalCarryingCapacityAlertNode entity = new EcologicalCarryingCapacityAlertNode(
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
    public Optional<EcologicalCarryingCapacityAlertNode> findEcologicalCarryingCapacityAlertNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EcologicalCarryingCapacityAlertNode processOptimization(String id, String tenantId) {
        EcologicalCarryingCapacityAlertNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EcologicalCarryingCapacityAlertNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
