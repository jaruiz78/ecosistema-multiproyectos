package com.corp.proyectoomnisystemicplanetarytwin.application.service;

import com.corp.proyectoomnisystemicplanetarytwin.domain.model.PlanetaryTensorsNexusNode;
import com.corp.proyectoomnisystemicplanetarytwin.domain.port.in.ManagePlanetaryTensorsNexusNodeUseCase;
import com.corp.proyectoomnisystemicplanetarytwin.domain.port.out.PlanetaryTensorsNexusNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PlanetaryTensorsNexusNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlanetaryTensorsNexusNodeApplicationService implements ManagePlanetaryTensorsNexusNodeUseCase {

    private final PlanetaryTensorsNexusNodeRepositoryPort repositoryPort;

    public PlanetaryTensorsNexusNodeApplicationService(PlanetaryTensorsNexusNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PlanetaryTensorsNexusNode createPlanetaryTensorsNexusNode(String tenantId, String title, double value) {
        PlanetaryTensorsNexusNode entity = new PlanetaryTensorsNexusNode(
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
    public Optional<PlanetaryTensorsNexusNode> findPlanetaryTensorsNexusNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PlanetaryTensorsNexusNode processOptimization(String id, String tenantId) {
        PlanetaryTensorsNexusNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PlanetaryTensorsNexusNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
