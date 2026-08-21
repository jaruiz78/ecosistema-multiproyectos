package com.corp.proyectoemergencygeogridcrisis.application.service;

import com.corp.proyectoemergencygeogridcrisis.domain.model.EmergencyEvacuationZone;
import com.corp.proyectoemergencygeogridcrisis.domain.port.in.ManageEmergencyEvacuationZoneUseCase;
import com.corp.proyectoemergencygeogridcrisis.domain.port.out.EmergencyEvacuationZoneRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EmergencyEvacuationZone.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class EmergencyEvacuationZoneApplicationService implements ManageEmergencyEvacuationZoneUseCase {

    private final EmergencyEvacuationZoneRepositoryPort repositoryPort;

    public EmergencyEvacuationZoneApplicationService(EmergencyEvacuationZoneRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EmergencyEvacuationZone createEmergencyEvacuationZone(String tenantId, String title, double value) {
        EmergencyEvacuationZone entity = new EmergencyEvacuationZone(
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
    public Optional<EmergencyEvacuationZone> findEmergencyEvacuationZoneById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EmergencyEvacuationZone processOptimization(String id, String tenantId) {
        EmergencyEvacuationZone existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EmergencyEvacuationZone optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
