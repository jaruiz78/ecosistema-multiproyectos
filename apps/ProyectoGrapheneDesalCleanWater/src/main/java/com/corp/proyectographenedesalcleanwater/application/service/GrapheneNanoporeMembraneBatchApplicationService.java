package com.corp.proyectographenedesalcleanwater.application.service;

import com.corp.proyectographenedesalcleanwater.domain.model.GrapheneNanoporeMembraneBatch;
import com.corp.proyectographenedesalcleanwater.domain.port.in.ManageGrapheneNanoporeMembraneBatchUseCase;
import com.corp.proyectographenedesalcleanwater.domain.port.out.GrapheneNanoporeMembraneBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GrapheneNanoporeMembraneBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GrapheneNanoporeMembraneBatchApplicationService implements ManageGrapheneNanoporeMembraneBatchUseCase {

    private final GrapheneNanoporeMembraneBatchRepositoryPort repositoryPort;

    public GrapheneNanoporeMembraneBatchApplicationService(GrapheneNanoporeMembraneBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GrapheneNanoporeMembraneBatch createGrapheneNanoporeMembraneBatch(String tenantId, String title, double value) {
        GrapheneNanoporeMembraneBatch entity = new GrapheneNanoporeMembraneBatch(
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
    public Optional<GrapheneNanoporeMembraneBatch> findGrapheneNanoporeMembraneBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GrapheneNanoporeMembraneBatch processOptimization(String id, String tenantId) {
        GrapheneNanoporeMembraneBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GrapheneNanoporeMembraneBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
