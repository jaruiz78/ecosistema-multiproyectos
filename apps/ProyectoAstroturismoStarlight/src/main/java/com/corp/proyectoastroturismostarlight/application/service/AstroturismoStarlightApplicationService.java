package com.corp.proyectoastroturismostarlight.application.service;

import com.corp.proyectoastroturismostarlight.domain.model.AstroturismoStarlight;
import com.corp.proyectoastroturismostarlight.domain.port.in.ManageAstroturismoStarlightUseCase;
import com.corp.proyectoastroturismostarlight.domain.port.out.AstroturismoStarlightRepositoryPort;
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
public class AstroturismoStarlightApplicationService implements ManageAstroturismoStarlightUseCase {

    private final AstroturismoStarlightRepositoryPort repositoryPort;

    public AstroturismoStarlightApplicationService(AstroturismoStarlightRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AstroturismoStarlight createAstroturismoStarlight(String tenantId, String title, double value) {
        AstroturismoStarlight entity = new AstroturismoStarlight(
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
    public Optional<AstroturismoStarlight> findAstroturismoStarlightById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AstroturismoStarlight processOptimization(String id, String tenantId) {
        AstroturismoStarlight existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AstroturismoStarlight optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
