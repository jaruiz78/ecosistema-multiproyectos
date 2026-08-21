package com.corp.proyectoomniplanetaryhypertwin.application.service;

import com.corp.proyectoomniplanetaryhypertwin.domain.model.HyperPlanetaryTensorNexusNode;
import com.corp.proyectoomniplanetaryhypertwin.domain.port.in.ManageHyperPlanetaryTensorNexusNodeUseCase;
import com.corp.proyectoomniplanetaryhypertwin.domain.port.out.HyperPlanetaryTensorNexusNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HyperPlanetaryTensorNexusNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HyperPlanetaryTensorNexusNodeApplicationService implements ManageHyperPlanetaryTensorNexusNodeUseCase {

    private final HyperPlanetaryTensorNexusNodeRepositoryPort repositoryPort;

    public HyperPlanetaryTensorNexusNodeApplicationService(HyperPlanetaryTensorNexusNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HyperPlanetaryTensorNexusNode createHyperPlanetaryTensorNexusNode(String tenantId, String title, double value) {
        HyperPlanetaryTensorNexusNode entity = new HyperPlanetaryTensorNexusNode(
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
    public Optional<HyperPlanetaryTensorNexusNode> findHyperPlanetaryTensorNexusNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HyperPlanetaryTensorNexusNode processOptimization(String id, String tenantId) {
        HyperPlanetaryTensorNexusNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HyperPlanetaryTensorNexusNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
