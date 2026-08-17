package com.corp.proyectoemergencygeogrid.application.service;

import com.corp.proyectoemergencygeogrid.domain.model.EmergencyGeoGrid;
import com.corp.proyectoemergencygeogrid.domain.port.in.ManageEmergencyGeoGridUseCase;
import com.corp.proyectoemergencygeogrid.domain.port.out.EmergencyGeoGridRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_8_geoespacial_h3_osrm_movilidad">FACULTAD_IX: Geoespacial H3, OSRM & Movilidad</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class EmergencyGeoGridApplicationService implements ManageEmergencyGeoGridUseCase {

    private final EmergencyGeoGridRepositoryPort repositoryPort;

    public EmergencyGeoGridApplicationService(EmergencyGeoGridRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EmergencyGeoGrid createEmergencyGeoGrid(String tenantId, String title, double value) {
        EmergencyGeoGrid entity = new EmergencyGeoGrid(
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
    public Optional<EmergencyGeoGrid> findEmergencyGeoGridById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EmergencyGeoGrid processOptimization(String id, String tenantId) {
        EmergencyGeoGrid existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EmergencyGeoGrid optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
