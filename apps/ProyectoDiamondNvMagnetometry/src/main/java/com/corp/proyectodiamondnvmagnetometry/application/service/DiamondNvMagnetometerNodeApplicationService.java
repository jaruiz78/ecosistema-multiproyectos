package com.corp.proyectodiamondnvmagnetometry.application.service;

import com.corp.proyectodiamondnvmagnetometry.domain.model.DiamondNvMagnetometerNode;
import com.corp.proyectodiamondnvmagnetometry.domain.port.in.ManageDiamondNvMagnetometerNodeUseCase;
import com.corp.proyectodiamondnvmagnetometry.domain.port.out.DiamondNvMagnetometerNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DiamondNvMagnetometerNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DiamondNvMagnetometerNodeApplicationService implements ManageDiamondNvMagnetometerNodeUseCase {

    private final DiamondNvMagnetometerNodeRepositoryPort repositoryPort;

    public DiamondNvMagnetometerNodeApplicationService(DiamondNvMagnetometerNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DiamondNvMagnetometerNode createDiamondNvMagnetometerNode(String tenantId, String title, double value) {
        DiamondNvMagnetometerNode entity = new DiamondNvMagnetometerNode(
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
    public Optional<DiamondNvMagnetometerNode> findDiamondNvMagnetometerNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DiamondNvMagnetometerNode processOptimization(String id, String tenantId) {
        DiamondNvMagnetometerNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DiamondNvMagnetometerNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
