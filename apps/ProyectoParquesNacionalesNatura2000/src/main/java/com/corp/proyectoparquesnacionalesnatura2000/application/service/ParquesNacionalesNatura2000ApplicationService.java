package com.corp.proyectoparquesnacionalesnatura2000.application.service;

import com.corp.proyectoparquesnacionalesnatura2000.domain.model.ParquesNacionalesNatura2000;
import com.corp.proyectoparquesnacionalesnatura2000.domain.port.in.ManageParquesNacionalesNatura2000UseCase;
import com.corp.proyectoparquesnacionalesnatura2000.domain.port.out.ParquesNacionalesNatura2000RepositoryPort;
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
public class ParquesNacionalesNatura2000ApplicationService implements ManageParquesNacionalesNatura2000UseCase {

    private final ParquesNacionalesNatura2000RepositoryPort repositoryPort;

    public ParquesNacionalesNatura2000ApplicationService(ParquesNacionalesNatura2000RepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ParquesNacionalesNatura2000 createParquesNacionalesNatura2000(String tenantId, String title, double value) {
        ParquesNacionalesNatura2000 entity = new ParquesNacionalesNatura2000(
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
    public Optional<ParquesNacionalesNatura2000> findParquesNacionalesNatura2000ById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ParquesNacionalesNatura2000 processOptimization(String id, String tenantId) {
        ParquesNacionalesNatura2000 existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ParquesNacionalesNatura2000 optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
