package com.corp.proyectosalud.application.service;

import com.corp.proyectosalud.domain.model.Salud;
import com.corp.proyectosalud.domain.port.in.ManageSaludUseCase;
import com.corp.proyectosalud.domain.port.out.SaludRepositoryPort;
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
public class SaludApplicationService implements ManageSaludUseCase {

    private final SaludRepositoryPort repositoryPort;

    public SaludApplicationService(SaludRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Salud createSalud(String tenantId, String title, double value) {
        Salud entity = new Salud(
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
    public Optional<Salud> findSaludById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Salud processOptimization(String id, String tenantId) {
        Salud existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Salud optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
