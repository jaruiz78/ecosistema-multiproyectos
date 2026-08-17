package com.corp.proyectoporttwinautonomous.application.service;

import com.corp.proyectoporttwinautonomous.domain.model.PortTwinAutonomous;
import com.corp.proyectoporttwinautonomous.domain.port.in.ManagePortTwinAutonomousUseCase;
import com.corp.proyectoporttwinautonomous.domain.port.out.PortTwinAutonomousRepositoryPort;
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
public class PortTwinAutonomousApplicationService implements ManagePortTwinAutonomousUseCase {

    private final PortTwinAutonomousRepositoryPort repositoryPort;

    public PortTwinAutonomousApplicationService(PortTwinAutonomousRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PortTwinAutonomous createPortTwinAutonomous(String tenantId, String title, double value) {
        PortTwinAutonomous entity = new PortTwinAutonomous(
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
    public Optional<PortTwinAutonomous> findPortTwinAutonomousById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PortTwinAutonomous processOptimization(String id, String tenantId) {
        PortTwinAutonomous existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PortTwinAutonomous optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
