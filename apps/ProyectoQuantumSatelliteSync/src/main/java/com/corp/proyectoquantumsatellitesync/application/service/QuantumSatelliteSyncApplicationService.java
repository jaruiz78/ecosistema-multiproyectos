package com.corp.proyectoquantumsatellitesync.application.service;

import com.corp.proyectoquantumsatellitesync.domain.model.QuantumSatelliteSync;
import com.corp.proyectoquantumsatellitesync.domain.port.in.ManageQuantumSatelliteSyncUseCase;
import com.corp.proyectoquantumsatellitesync.domain.port.out.QuantumSatelliteSyncRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class QuantumSatelliteSyncApplicationService implements ManageQuantumSatelliteSyncUseCase {

    private final QuantumSatelliteSyncRepositoryPort repositoryPort;

    public QuantumSatelliteSyncApplicationService(QuantumSatelliteSyncRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuantumSatelliteSync createQuantumSatelliteSync(String tenantId, String title, double value) {
        QuantumSatelliteSync entity = new QuantumSatelliteSync(
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
    public Optional<QuantumSatelliteSync> findQuantumSatelliteSyncById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuantumSatelliteSync processOptimization(String id, String tenantId) {
        QuantumSatelliteSync existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuantumSatelliteSync optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
