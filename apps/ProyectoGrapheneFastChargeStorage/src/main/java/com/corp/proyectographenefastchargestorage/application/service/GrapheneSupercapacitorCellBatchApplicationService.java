package com.corp.proyectographenefastchargestorage.application.service;

import com.corp.proyectographenefastchargestorage.domain.model.GrapheneSupercapacitorCellBatch;
import com.corp.proyectographenefastchargestorage.domain.port.in.ManageGrapheneSupercapacitorCellBatchUseCase;
import com.corp.proyectographenefastchargestorage.domain.port.out.GrapheneSupercapacitorCellBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GrapheneSupercapacitorCellBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GrapheneSupercapacitorCellBatchApplicationService implements ManageGrapheneSupercapacitorCellBatchUseCase {

    private final GrapheneSupercapacitorCellBatchRepositoryPort repositoryPort;

    public GrapheneSupercapacitorCellBatchApplicationService(GrapheneSupercapacitorCellBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GrapheneSupercapacitorCellBatch createGrapheneSupercapacitorCellBatch(String tenantId, String title, double value) {
        GrapheneSupercapacitorCellBatch entity = new GrapheneSupercapacitorCellBatch(
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
    public Optional<GrapheneSupercapacitorCellBatch> findGrapheneSupercapacitorCellBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GrapheneSupercapacitorCellBatch processOptimization(String id, String tenantId) {
        GrapheneSupercapacitorCellBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GrapheneSupercapacitorCellBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
