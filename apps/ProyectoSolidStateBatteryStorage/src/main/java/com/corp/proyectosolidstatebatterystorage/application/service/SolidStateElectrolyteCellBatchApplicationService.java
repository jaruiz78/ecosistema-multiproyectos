package com.corp.proyectosolidstatebatterystorage.application.service;

import com.corp.proyectosolidstatebatterystorage.domain.model.SolidStateElectrolyteCellBatch;
import com.corp.proyectosolidstatebatterystorage.domain.port.in.ManageSolidStateElectrolyteCellBatchUseCase;
import com.corp.proyectosolidstatebatterystorage.domain.port.out.SolidStateElectrolyteCellBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SolidStateElectrolyteCellBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SolidStateElectrolyteCellBatchApplicationService implements ManageSolidStateElectrolyteCellBatchUseCase {

    private final SolidStateElectrolyteCellBatchRepositoryPort repositoryPort;

    public SolidStateElectrolyteCellBatchApplicationService(SolidStateElectrolyteCellBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SolidStateElectrolyteCellBatch createSolidStateElectrolyteCellBatch(String tenantId, String title, double value) {
        SolidStateElectrolyteCellBatch entity = new SolidStateElectrolyteCellBatch(
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
    public Optional<SolidStateElectrolyteCellBatch> findSolidStateElectrolyteCellBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SolidStateElectrolyteCellBatch processOptimization(String id, String tenantId) {
        SolidStateElectrolyteCellBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SolidStateElectrolyteCellBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
