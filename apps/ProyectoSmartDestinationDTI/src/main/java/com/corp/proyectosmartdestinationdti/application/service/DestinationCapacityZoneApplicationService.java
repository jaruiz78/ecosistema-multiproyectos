package com.corp.proyectosmartdestinationdti.application.service;

import com.corp.proyectosmartdestinationdti.domain.model.DestinationCapacityZone;
import com.corp.proyectosmartdestinationdti.domain.port.in.ManageDestinationCapacityZoneUseCase;
import com.corp.proyectosmartdestinationdti.domain.port.out.DestinationCapacityZoneRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DestinationCapacityZone.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DestinationCapacityZoneApplicationService implements ManageDestinationCapacityZoneUseCase {

    private final DestinationCapacityZoneRepositoryPort repositoryPort;

    public DestinationCapacityZoneApplicationService(DestinationCapacityZoneRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DestinationCapacityZone createDestinationCapacityZone(String tenantId, String title, double value) {
        DestinationCapacityZone entity = new DestinationCapacityZone(
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
    public Optional<DestinationCapacityZone> findDestinationCapacityZoneById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DestinationCapacityZone processOptimization(String id, String tenantId) {
        DestinationCapacityZone existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DestinationCapacityZone optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
