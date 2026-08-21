package com.corp.proyectocryoagrifoodlogistics.application.service;

import com.corp.proyectocryoagrifoodlogistics.domain.model.CryogenicTelemetryBatchNode;
import com.corp.proyectocryoagrifoodlogistics.domain.port.in.ManageCryogenicTelemetryBatchNodeUseCase;
import com.corp.proyectocryoagrifoodlogistics.domain.port.out.CryogenicTelemetryBatchNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CryogenicTelemetryBatchNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CryogenicTelemetryBatchNodeApplicationService implements ManageCryogenicTelemetryBatchNodeUseCase {

    private final CryogenicTelemetryBatchNodeRepositoryPort repositoryPort;

    public CryogenicTelemetryBatchNodeApplicationService(CryogenicTelemetryBatchNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CryogenicTelemetryBatchNode createCryogenicTelemetryBatchNode(String tenantId, String title, double value) {
        CryogenicTelemetryBatchNode entity = new CryogenicTelemetryBatchNode(
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
    public Optional<CryogenicTelemetryBatchNode> findCryogenicTelemetryBatchNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CryogenicTelemetryBatchNode processOptimization(String id, String tenantId) {
        CryogenicTelemetryBatchNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CryogenicTelemetryBatchNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
