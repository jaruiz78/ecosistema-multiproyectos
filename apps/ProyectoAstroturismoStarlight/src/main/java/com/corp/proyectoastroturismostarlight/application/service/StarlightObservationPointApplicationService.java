package com.corp.proyectoastroturismostarlight.application.service;

import com.corp.proyectoastroturismostarlight.domain.model.StarlightObservationPoint;
import com.corp.proyectoastroturismostarlight.domain.port.in.ManageStarlightObservationPointUseCase;
import com.corp.proyectoastroturismostarlight.domain.port.out.StarlightObservationPointRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de StarlightObservationPoint.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class StarlightObservationPointApplicationService implements ManageStarlightObservationPointUseCase {

    private final StarlightObservationPointRepositoryPort repositoryPort;

    public StarlightObservationPointApplicationService(StarlightObservationPointRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public StarlightObservationPoint createStarlightObservationPoint(String tenantId, String title, double value) {
        StarlightObservationPoint entity = new StarlightObservationPoint(
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
    public Optional<StarlightObservationPoint> findStarlightObservationPointById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public StarlightObservationPoint processOptimization(String id, String tenantId) {
        StarlightObservationPoint existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        StarlightObservationPoint optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
