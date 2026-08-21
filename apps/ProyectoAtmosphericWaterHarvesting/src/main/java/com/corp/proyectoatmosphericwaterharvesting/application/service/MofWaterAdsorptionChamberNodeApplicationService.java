package com.corp.proyectoatmosphericwaterharvesting.application.service;

import com.corp.proyectoatmosphericwaterharvesting.domain.model.MofWaterAdsorptionChamberNode;
import com.corp.proyectoatmosphericwaterharvesting.domain.port.in.ManageMofWaterAdsorptionChamberNodeUseCase;
import com.corp.proyectoatmosphericwaterharvesting.domain.port.out.MofWaterAdsorptionChamberNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MofWaterAdsorptionChamberNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MofWaterAdsorptionChamberNodeApplicationService implements ManageMofWaterAdsorptionChamberNodeUseCase {

    private final MofWaterAdsorptionChamberNodeRepositoryPort repositoryPort;

    public MofWaterAdsorptionChamberNodeApplicationService(MofWaterAdsorptionChamberNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MofWaterAdsorptionChamberNode createMofWaterAdsorptionChamberNode(String tenantId, String title, double value) {
        MofWaterAdsorptionChamberNode entity = new MofWaterAdsorptionChamberNode(
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
    public Optional<MofWaterAdsorptionChamberNode> findMofWaterAdsorptionChamberNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MofWaterAdsorptionChamberNode processOptimization(String id, String tenantId) {
        MofWaterAdsorptionChamberNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MofWaterAdsorptionChamberNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
