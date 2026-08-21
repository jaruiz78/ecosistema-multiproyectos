package com.corp.proyectoagrifoodcoldchaintrace.application.service;

import com.corp.proyectoagrifoodcoldchaintrace.domain.model.ColdChainShipmentBatch;
import com.corp.proyectoagrifoodcoldchaintrace.domain.port.in.ManageColdChainShipmentBatchUseCase;
import com.corp.proyectoagrifoodcoldchaintrace.domain.port.out.ColdChainShipmentBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de ColdChainShipmentBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class ColdChainShipmentBatchApplicationService implements ManageColdChainShipmentBatchUseCase {

    private final ColdChainShipmentBatchRepositoryPort repositoryPort;

    public ColdChainShipmentBatchApplicationService(ColdChainShipmentBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ColdChainShipmentBatch createColdChainShipmentBatch(String tenantId, String title, double value) {
        ColdChainShipmentBatch entity = new ColdChainShipmentBatch(
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
    public Optional<ColdChainShipmentBatch> findColdChainShipmentBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ColdChainShipmentBatch processOptimization(String id, String tenantId) {
        ColdChainShipmentBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ColdChainShipmentBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
