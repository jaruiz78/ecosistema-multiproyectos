package com.corp.proyectob2g.application.service;

import com.corp.proyectob2g.domain.model.B2G;
import com.corp.proyectob2g.domain.port.in.ManageB2GUseCase;
import com.corp.proyectob2g.domain.port.out.B2GRepositoryPort;
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
public class B2GApplicationService implements ManageB2GUseCase {

    private final B2GRepositoryPort repositoryPort;

    public B2GApplicationService(B2GRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public B2G createB2G(String tenantId, String title, double value) {
        B2G entity = new B2G(
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
    public Optional<B2G> findB2GById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public B2G processOptimization(String id, String tenantId) {
        B2G existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        B2G optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
