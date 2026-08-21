package com.corp.proyectocubesatconstellationadcs.application.service;

import com.corp.proyectocubesatconstellationadcs.domain.model.CubesatAttitudeQuaternionPointingNode;
import com.corp.proyectocubesatconstellationadcs.domain.port.in.ManageCubesatAttitudeQuaternionPointingNodeUseCase;
import com.corp.proyectocubesatconstellationadcs.domain.port.out.CubesatAttitudeQuaternionPointingNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CubesatAttitudeQuaternionPointingNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CubesatAttitudeQuaternionPointingNodeApplicationService implements ManageCubesatAttitudeQuaternionPointingNodeUseCase {

    private final CubesatAttitudeQuaternionPointingNodeRepositoryPort repositoryPort;

    public CubesatAttitudeQuaternionPointingNodeApplicationService(CubesatAttitudeQuaternionPointingNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CubesatAttitudeQuaternionPointingNode createCubesatAttitudeQuaternionPointingNode(String tenantId, String title, double value) {
        CubesatAttitudeQuaternionPointingNode entity = new CubesatAttitudeQuaternionPointingNode(
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
    public Optional<CubesatAttitudeQuaternionPointingNode> findCubesatAttitudeQuaternionPointingNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CubesatAttitudeQuaternionPointingNode processOptimization(String id, String tenantId) {
        CubesatAttitudeQuaternionPointingNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CubesatAttitudeQuaternionPointingNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
