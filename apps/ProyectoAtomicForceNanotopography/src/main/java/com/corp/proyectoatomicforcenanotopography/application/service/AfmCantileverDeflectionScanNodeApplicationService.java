package com.corp.proyectoatomicforcenanotopography.application.service;

import com.corp.proyectoatomicforcenanotopography.domain.model.AfmCantileverDeflectionScanNode;
import com.corp.proyectoatomicforcenanotopography.domain.port.in.ManageAfmCantileverDeflectionScanNodeUseCase;
import com.corp.proyectoatomicforcenanotopography.domain.port.out.AfmCantileverDeflectionScanNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AfmCantileverDeflectionScanNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AfmCantileverDeflectionScanNodeApplicationService implements ManageAfmCantileverDeflectionScanNodeUseCase {

    private final AfmCantileverDeflectionScanNodeRepositoryPort repositoryPort;

    public AfmCantileverDeflectionScanNodeApplicationService(AfmCantileverDeflectionScanNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AfmCantileverDeflectionScanNode createAfmCantileverDeflectionScanNode(String tenantId, String title, double value) {
        AfmCantileverDeflectionScanNode entity = new AfmCantileverDeflectionScanNode(
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
    public Optional<AfmCantileverDeflectionScanNode> findAfmCantileverDeflectionScanNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AfmCantileverDeflectionScanNode processOptimization(String id, String tenantId) {
        AfmCantileverDeflectionScanNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AfmCantileverDeflectionScanNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
