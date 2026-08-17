package com.corp.proyectoagrobiorobotics.application.service;

import com.corp.proyectoagrobiorobotics.domain.model.AgroBioRobotics;
import com.corp.proyectoagrobiorobotics.domain.port.in.ManageAgroBioRoboticsUseCase;
import com.corp.proyectoagrobiorobotics.domain.port.out.AgroBioRoboticsRepositoryPort;
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
public class AgroBioRoboticsApplicationService implements ManageAgroBioRoboticsUseCase {

    private final AgroBioRoboticsRepositoryPort repositoryPort;

    public AgroBioRoboticsApplicationService(AgroBioRoboticsRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AgroBioRobotics createAgroBioRobotics(String tenantId, String title, double value) {
        AgroBioRobotics entity = new AgroBioRobotics(
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
    public Optional<AgroBioRobotics> findAgroBioRoboticsById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AgroBioRobotics processOptimization(String id, String tenantId) {
        AgroBioRobotics existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AgroBioRobotics optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
