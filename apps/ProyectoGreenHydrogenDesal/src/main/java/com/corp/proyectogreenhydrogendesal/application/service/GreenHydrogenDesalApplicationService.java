package com.corp.proyectogreenhydrogendesal.application.service;

import com.corp.proyectogreenhydrogendesal.domain.model.GreenHydrogenDesal;
import com.corp.proyectogreenhydrogendesal.domain.port.in.ManageGreenHydrogenDesalUseCase;
import com.corp.proyectogreenhydrogendesal.domain.port.out.GreenHydrogenDesalRepositoryPort;
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
public class GreenHydrogenDesalApplicationService implements ManageGreenHydrogenDesalUseCase {

    private final GreenHydrogenDesalRepositoryPort repositoryPort;

    public GreenHydrogenDesalApplicationService(GreenHydrogenDesalRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GreenHydrogenDesal createGreenHydrogenDesal(String tenantId, String title, double value) {
        GreenHydrogenDesal entity = new GreenHydrogenDesal(
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
    public Optional<GreenHydrogenDesal> findGreenHydrogenDesalById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GreenHydrogenDesal processOptimization(String id, String tenantId) {
        GreenHydrogenDesal existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GreenHydrogenDesal optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
