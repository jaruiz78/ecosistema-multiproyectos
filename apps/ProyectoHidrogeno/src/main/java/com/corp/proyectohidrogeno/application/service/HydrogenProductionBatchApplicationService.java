package com.corp.proyectohidrogeno.application.service;

import com.corp.proyectohidrogeno.domain.model.HydrogenProductionBatch;
import com.corp.proyectohidrogeno.domain.port.in.ManageHydrogenProductionBatchUseCase;
import com.corp.proyectohidrogeno.domain.port.out.HydrogenProductionBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HydrogenProductionBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HydrogenProductionBatchApplicationService implements ManageHydrogenProductionBatchUseCase {

    private final HydrogenProductionBatchRepositoryPort repositoryPort;

    public HydrogenProductionBatchApplicationService(HydrogenProductionBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HydrogenProductionBatch createHydrogenProductionBatch(String tenantId, String title, double value) {
        HydrogenProductionBatch entity = new HydrogenProductionBatch(
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
    public Optional<HydrogenProductionBatch> findHydrogenProductionBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HydrogenProductionBatch processOptimization(String id, String tenantId) {
        HydrogenProductionBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HydrogenProductionBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
