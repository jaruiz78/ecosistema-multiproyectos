package com.corp.proyectoplayasinteligentescostas.application.service;

import com.corp.proyectoplayasinteligentescostas.domain.model.PlayasInteligentesCostas;
import com.corp.proyectoplayasinteligentescostas.domain.port.in.ManagePlayasInteligentesCostasUseCase;
import com.corp.proyectoplayasinteligentescostas.domain.port.out.PlayasInteligentesCostasRepositoryPort;
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
public class PlayasInteligentesCostasApplicationService implements ManagePlayasInteligentesCostasUseCase {

    private final PlayasInteligentesCostasRepositoryPort repositoryPort;

    public PlayasInteligentesCostasApplicationService(PlayasInteligentesCostasRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PlayasInteligentesCostas createPlayasInteligentesCostas(String tenantId, String title, double value) {
        PlayasInteligentesCostas entity = new PlayasInteligentesCostas(
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
    public Optional<PlayasInteligentesCostas> findPlayasInteligentesCostasById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PlayasInteligentesCostas processOptimization(String id, String tenantId) {
        PlayasInteligentesCostas existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PlayasInteligentesCostas optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
