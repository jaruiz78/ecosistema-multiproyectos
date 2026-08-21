package com.corp.proyectomicrobialelectrosynthesisbiofuel.application.service;

import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.model.CathodeBiofilmElectronUptakeNode;
import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.in.ManageCathodeBiofilmElectronUptakeNodeUseCase;
import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.out.CathodeBiofilmElectronUptakeNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CathodeBiofilmElectronUptakeNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CathodeBiofilmElectronUptakeNodeApplicationService implements ManageCathodeBiofilmElectronUptakeNodeUseCase {

    private final CathodeBiofilmElectronUptakeNodeRepositoryPort repositoryPort;

    public CathodeBiofilmElectronUptakeNodeApplicationService(CathodeBiofilmElectronUptakeNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CathodeBiofilmElectronUptakeNode createCathodeBiofilmElectronUptakeNode(String tenantId, String title, double value) {
        CathodeBiofilmElectronUptakeNode entity = new CathodeBiofilmElectronUptakeNode(
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
    public Optional<CathodeBiofilmElectronUptakeNode> findCathodeBiofilmElectronUptakeNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CathodeBiofilmElectronUptakeNode processOptimization(String id, String tenantId) {
        CathodeBiofilmElectronUptakeNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CathodeBiofilmElectronUptakeNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
