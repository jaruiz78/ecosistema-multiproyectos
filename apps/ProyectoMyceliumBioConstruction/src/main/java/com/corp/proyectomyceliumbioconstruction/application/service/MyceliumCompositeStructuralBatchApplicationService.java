package com.corp.proyectomyceliumbioconstruction.application.service;

import com.corp.proyectomyceliumbioconstruction.domain.model.MyceliumCompositeStructuralBatch;
import com.corp.proyectomyceliumbioconstruction.domain.port.in.ManageMyceliumCompositeStructuralBatchUseCase;
import com.corp.proyectomyceliumbioconstruction.domain.port.out.MyceliumCompositeStructuralBatchRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MyceliumCompositeStructuralBatch.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MyceliumCompositeStructuralBatchApplicationService implements ManageMyceliumCompositeStructuralBatchUseCase {

    private final MyceliumCompositeStructuralBatchRepositoryPort repositoryPort;

    public MyceliumCompositeStructuralBatchApplicationService(MyceliumCompositeStructuralBatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MyceliumCompositeStructuralBatch createMyceliumCompositeStructuralBatch(String tenantId, String title, double value) {
        MyceliumCompositeStructuralBatch entity = new MyceliumCompositeStructuralBatch(
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
    public Optional<MyceliumCompositeStructuralBatch> findMyceliumCompositeStructuralBatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MyceliumCompositeStructuralBatch processOptimization(String id, String tenantId) {
        MyceliumCompositeStructuralBatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MyceliumCompositeStructuralBatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
