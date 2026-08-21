package com.corp.proyectocircularbiomassbiorefinery.application.service;

import com.corp.proyectocircularbiomassbiorefinery.domain.model.BiomassFeedstockBatch;
import com.corp.proyectocircularbiomassbiorefinery.domain.port.in.ManageBiomassFeedstockBatchUseCase;
import com.corp.proyectocircularbiomassbiorefinery.domain.port.out.BiomassFeedstockBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de BiomassFeedstockBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class BiomassFeedstockBatchApplicationService implements ManageBiomassFeedstockBatchUseCase {

    private final BiomassFeedstockBatchRepositoryPort repositoryPort;

    public BiomassFeedstockBatchApplicationService(BiomassFeedstockBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BiomassFeedstockBatch createBiomassFeedstockBatch(String tenantId, String title, double value) {
        BiomassFeedstockBatch entity = new BiomassFeedstockBatch(
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
    public Optional<BiomassFeedstockBatch> findBiomassFeedstockBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BiomassFeedstockBatch processOptimization(String id, String tenantId) {
        BiomassFeedstockBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BiomassFeedstockBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
