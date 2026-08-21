package com.corp.proyectocoralreefecosystempreserve.application.service;

import com.corp.proyectocoralreefecosystempreserve.domain.model.CoralBleachingDegreeWeekNode;
import com.corp.proyectocoralreefecosystempreserve.domain.port.in.ManageCoralBleachingDegreeWeekNodeUseCase;
import com.corp.proyectocoralreefecosystempreserve.domain.port.out.CoralBleachingDegreeWeekNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CoralBleachingDegreeWeekNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CoralBleachingDegreeWeekNodeApplicationService implements ManageCoralBleachingDegreeWeekNodeUseCase {

    private final CoralBleachingDegreeWeekNodeRepositoryPort repositoryPort;

    public CoralBleachingDegreeWeekNodeApplicationService(CoralBleachingDegreeWeekNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CoralBleachingDegreeWeekNode createCoralBleachingDegreeWeekNode(String tenantId, String title, double value) {
        CoralBleachingDegreeWeekNode entity = new CoralBleachingDegreeWeekNode(
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
    public Optional<CoralBleachingDegreeWeekNode> findCoralBleachingDegreeWeekNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CoralBleachingDegreeWeekNode processOptimization(String id, String tenantId) {
        CoralBleachingDegreeWeekNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CoralBleachingDegreeWeekNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
