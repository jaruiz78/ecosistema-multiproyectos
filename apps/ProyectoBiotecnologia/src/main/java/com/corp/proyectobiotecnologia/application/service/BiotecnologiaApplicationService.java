package com.corp.proyectobiotecnologia.application.service;

import com.corp.proyectobiotecnologia.domain.model.Biotecnologia;
import com.corp.proyectobiotecnologia.domain.port.in.ManageBiotecnologiaUseCase;
import com.corp.proyectobiotecnologia.domain.port.out.BiotecnologiaRepositoryPort;
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
public class BiotecnologiaApplicationService implements ManageBiotecnologiaUseCase {

    private final BiotecnologiaRepositoryPort repositoryPort;

    public BiotecnologiaApplicationService(BiotecnologiaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Biotecnologia createBiotecnologia(String tenantId, String title, double value) {
        Biotecnologia entity = new Biotecnologia(
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
    public Optional<Biotecnologia> findBiotecnologiaById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Biotecnologia processOptimization(String id, String tenantId) {
        Biotecnologia existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Biotecnologia optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
