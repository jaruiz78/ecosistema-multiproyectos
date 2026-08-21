package com.corp.proyectovolcanicashairspacesafety.application.service;

import com.corp.proyectovolcanicashairspacesafety.domain.model.VolcanicAshConcentrationFlightLevelNode;
import com.corp.proyectovolcanicashairspacesafety.domain.port.in.ManageVolcanicAshConcentrationFlightLevelNodeUseCase;
import com.corp.proyectovolcanicashairspacesafety.domain.port.out.VolcanicAshConcentrationFlightLevelNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VolcanicAshConcentrationFlightLevelNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class VolcanicAshConcentrationFlightLevelNodeApplicationService implements ManageVolcanicAshConcentrationFlightLevelNodeUseCase {

    private final VolcanicAshConcentrationFlightLevelNodeRepositoryPort repositoryPort;

    public VolcanicAshConcentrationFlightLevelNodeApplicationService(VolcanicAshConcentrationFlightLevelNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VolcanicAshConcentrationFlightLevelNode createVolcanicAshConcentrationFlightLevelNode(String tenantId, String title, double value) {
        VolcanicAshConcentrationFlightLevelNode entity = new VolcanicAshConcentrationFlightLevelNode(
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
    public Optional<VolcanicAshConcentrationFlightLevelNode> findVolcanicAshConcentrationFlightLevelNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VolcanicAshConcentrationFlightLevelNode processOptimization(String id, String tenantId) {
        VolcanicAshConcentrationFlightLevelNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VolcanicAshConcentrationFlightLevelNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
