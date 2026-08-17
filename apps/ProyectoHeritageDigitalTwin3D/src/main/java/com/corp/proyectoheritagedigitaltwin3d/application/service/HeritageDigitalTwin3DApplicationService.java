package com.corp.proyectoheritagedigitaltwin3d.application.service;

import com.corp.proyectoheritagedigitaltwin3d.domain.model.HeritageDigitalTwin3D;
import com.corp.proyectoheritagedigitaltwin3d.domain.port.in.ManageHeritageDigitalTwin3DUseCase;
import com.corp.proyectoheritagedigitaltwin3d.domain.port.out.HeritageDigitalTwin3DRepositoryPort;
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
public class HeritageDigitalTwin3DApplicationService implements ManageHeritageDigitalTwin3DUseCase {

    private final HeritageDigitalTwin3DRepositoryPort repositoryPort;

    public HeritageDigitalTwin3DApplicationService(HeritageDigitalTwin3DRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HeritageDigitalTwin3D createHeritageDigitalTwin3D(String tenantId, String title, double value) {
        HeritageDigitalTwin3D entity = new HeritageDigitalTwin3D(
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
    public Optional<HeritageDigitalTwin3D> findHeritageDigitalTwin3DById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HeritageDigitalTwin3D processOptimization(String id, String tenantId) {
        HeritageDigitalTwin3D existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HeritageDigitalTwin3D optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
