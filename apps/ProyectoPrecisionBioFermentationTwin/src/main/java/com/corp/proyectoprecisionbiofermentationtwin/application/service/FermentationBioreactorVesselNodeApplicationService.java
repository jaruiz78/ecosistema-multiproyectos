package com.corp.proyectoprecisionbiofermentationtwin.application.service;

import com.corp.proyectoprecisionbiofermentationtwin.domain.model.FermentationBioreactorVesselNode;
import com.corp.proyectoprecisionbiofermentationtwin.domain.port.in.ManageFermentationBioreactorVesselNodeUseCase;
import com.corp.proyectoprecisionbiofermentationtwin.domain.port.out.FermentationBioreactorVesselNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de FermentationBioreactorVesselNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class FermentationBioreactorVesselNodeApplicationService implements ManageFermentationBioreactorVesselNodeUseCase {

    private final FermentationBioreactorVesselNodeRepositoryPort repositoryPort;

    public FermentationBioreactorVesselNodeApplicationService(FermentationBioreactorVesselNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public FermentationBioreactorVesselNode createFermentationBioreactorVesselNode(String tenantId, String title, double value) {
        FermentationBioreactorVesselNode entity = new FermentationBioreactorVesselNode(
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
    public Optional<FermentationBioreactorVesselNode> findFermentationBioreactorVesselNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public FermentationBioreactorVesselNode processOptimization(String id, String tenantId) {
        FermentationBioreactorVesselNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        FermentationBioreactorVesselNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
