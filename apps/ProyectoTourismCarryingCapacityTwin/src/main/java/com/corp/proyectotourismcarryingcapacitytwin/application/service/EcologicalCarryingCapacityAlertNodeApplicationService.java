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
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
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
