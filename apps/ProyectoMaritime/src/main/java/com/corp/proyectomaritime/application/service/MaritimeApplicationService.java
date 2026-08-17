package com.corp.proyectomaritime.application.service;

import com.corp.proyectomaritime.domain.model.Maritime;
import com.corp.proyectomaritime.domain.port.in.ManageMaritimeUseCase;
import com.corp.proyectomaritime.domain.port.out.MaritimeRepositoryPort;
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
public class MaritimeApplicationService implements ManageMaritimeUseCase {

    private final MaritimeRepositoryPort repositoryPort;

    public MaritimeApplicationService(MaritimeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Maritime createMaritime(String tenantId, String title, double value) {
        Maritime entity = new Maritime(
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
    public Optional<Maritime> findMaritimeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Maritime processOptimization(String id, String tenantId) {
        Maritime existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Maritime optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
