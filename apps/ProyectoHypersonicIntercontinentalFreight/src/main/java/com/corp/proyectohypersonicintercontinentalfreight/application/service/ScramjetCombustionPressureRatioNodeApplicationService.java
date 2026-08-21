package com.corp.proyectohypersonicintercontinentalfreight.application.service;

import com.corp.proyectohypersonicintercontinentalfreight.domain.model.ScramjetCombustionPressureRatioNode;
import com.corp.proyectohypersonicintercontinentalfreight.domain.port.in.ManageScramjetCombustionPressureRatioNodeUseCase;
import com.corp.proyectohypersonicintercontinentalfreight.domain.port.out.ScramjetCombustionPressureRatioNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ScramjetCombustionPressureRatioNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ScramjetCombustionPressureRatioNodeApplicationService implements ManageScramjetCombustionPressureRatioNodeUseCase {

    private final ScramjetCombustionPressureRatioNodeRepositoryPort repositoryPort;

    public ScramjetCombustionPressureRatioNodeApplicationService(ScramjetCombustionPressureRatioNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ScramjetCombustionPressureRatioNode createScramjetCombustionPressureRatioNode(String tenantId, String title, double value) {
        ScramjetCombustionPressureRatioNode entity = new ScramjetCombustionPressureRatioNode(
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
    public Optional<ScramjetCombustionPressureRatioNode> findScramjetCombustionPressureRatioNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ScramjetCombustionPressureRatioNode processOptimization(String id, String tenantId) {
        ScramjetCombustionPressureRatioNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ScramjetCombustionPressureRatioNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
