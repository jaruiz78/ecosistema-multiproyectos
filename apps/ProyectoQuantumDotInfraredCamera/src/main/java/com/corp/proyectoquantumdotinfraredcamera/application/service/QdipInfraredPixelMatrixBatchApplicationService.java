package com.corp.proyectoquantumdotinfraredcamera.application.service;

import com.corp.proyectoquantumdotinfraredcamera.domain.model.QdipInfraredPixelMatrixBatch;
import com.corp.proyectoquantumdotinfraredcamera.domain.port.in.ManageQdipInfraredPixelMatrixBatchUseCase;
import com.corp.proyectoquantumdotinfraredcamera.domain.port.out.QdipInfraredPixelMatrixBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de QdipInfraredPixelMatrixBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class QdipInfraredPixelMatrixBatchApplicationService implements ManageQdipInfraredPixelMatrixBatchUseCase {

    private final QdipInfraredPixelMatrixBatchRepositoryPort repositoryPort;

    public QdipInfraredPixelMatrixBatchApplicationService(QdipInfraredPixelMatrixBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QdipInfraredPixelMatrixBatch createQdipInfraredPixelMatrixBatch(String tenantId, String title, double value) {
        QdipInfraredPixelMatrixBatch entity = new QdipInfraredPixelMatrixBatch(
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
    public Optional<QdipInfraredPixelMatrixBatch> findQdipInfraredPixelMatrixBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QdipInfraredPixelMatrixBatch processOptimization(String id, String tenantId) {
        QdipInfraredPixelMatrixBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QdipInfraredPixelMatrixBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
