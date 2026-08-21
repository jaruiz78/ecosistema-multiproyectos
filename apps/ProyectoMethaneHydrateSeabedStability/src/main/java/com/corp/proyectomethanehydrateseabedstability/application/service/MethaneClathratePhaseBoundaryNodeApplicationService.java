package com.corp.proyectomethanehydrateseabedstability.application.service;

import com.corp.proyectomethanehydrateseabedstability.domain.model.MethaneClathratePhaseBoundaryNode;
import com.corp.proyectomethanehydrateseabedstability.domain.port.in.ManageMethaneClathratePhaseBoundaryNodeUseCase;
import com.corp.proyectomethanehydrateseabedstability.domain.port.out.MethaneClathratePhaseBoundaryNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MethaneClathratePhaseBoundaryNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MethaneClathratePhaseBoundaryNodeApplicationService implements ManageMethaneClathratePhaseBoundaryNodeUseCase {

    private final MethaneClathratePhaseBoundaryNodeRepositoryPort repositoryPort;

    public MethaneClathratePhaseBoundaryNodeApplicationService(MethaneClathratePhaseBoundaryNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MethaneClathratePhaseBoundaryNode createMethaneClathratePhaseBoundaryNode(String tenantId, String title, double value) {
        MethaneClathratePhaseBoundaryNode entity = new MethaneClathratePhaseBoundaryNode(
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
    public Optional<MethaneClathratePhaseBoundaryNode> findMethaneClathratePhaseBoundaryNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MethaneClathratePhaseBoundaryNode processOptimization(String id, String tenantId) {
        MethaneClathratePhaseBoundaryNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MethaneClathratePhaseBoundaryNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
