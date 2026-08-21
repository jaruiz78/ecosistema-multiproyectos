package com.corp.proyectomicroalgaecarbonbiofuel.application.service;

import com.corp.proyectomicroalgaecarbonbiofuel.domain.model.AlgaeBioreactorCultureBatch;
import com.corp.proyectomicroalgaecarbonbiofuel.domain.port.in.ManageAlgaeBioreactorCultureBatchUseCase;
import com.corp.proyectomicroalgaecarbonbiofuel.domain.port.out.AlgaeBioreactorCultureBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AlgaeBioreactorCultureBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AlgaeBioreactorCultureBatchApplicationService implements ManageAlgaeBioreactorCultureBatchUseCase {

    private final AlgaeBioreactorCultureBatchRepositoryPort repositoryPort;

    public AlgaeBioreactorCultureBatchApplicationService(AlgaeBioreactorCultureBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AlgaeBioreactorCultureBatch createAlgaeBioreactorCultureBatch(String tenantId, String title, double value) {
        AlgaeBioreactorCultureBatch entity = new AlgaeBioreactorCultureBatch(
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
    public Optional<AlgaeBioreactorCultureBatch> findAlgaeBioreactorCultureBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AlgaeBioreactorCultureBatch processOptimization(String id, String tenantId) {
        AlgaeBioreactorCultureBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AlgaeBioreactorCultureBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
