package com.corp.proyectoacousticmetamaterialshield.application.service;

import com.corp.proyectoacousticmetamaterialshield.domain.model.AcousticScatteringCancellationNode;
import com.corp.proyectoacousticmetamaterialshield.domain.port.in.ManageAcousticScatteringCancellationNodeUseCase;
import com.corp.proyectoacousticmetamaterialshield.domain.port.out.AcousticScatteringCancellationNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AcousticScatteringCancellationNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AcousticScatteringCancellationNodeApplicationService implements ManageAcousticScatteringCancellationNodeUseCase {

    private final AcousticScatteringCancellationNodeRepositoryPort repositoryPort;

    public AcousticScatteringCancellationNodeApplicationService(AcousticScatteringCancellationNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AcousticScatteringCancellationNode createAcousticScatteringCancellationNode(String tenantId, String title, double value) {
        AcousticScatteringCancellationNode entity = new AcousticScatteringCancellationNode(
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
    public Optional<AcousticScatteringCancellationNode> findAcousticScatteringCancellationNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AcousticScatteringCancellationNode processOptimization(String id, String tenantId) {
        AcousticScatteringCancellationNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AcousticScatteringCancellationNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
