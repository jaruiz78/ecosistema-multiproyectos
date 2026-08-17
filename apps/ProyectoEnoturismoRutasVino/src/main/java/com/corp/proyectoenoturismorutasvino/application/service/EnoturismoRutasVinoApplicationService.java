package com.corp.proyectoenoturismorutasvino.application.service;

import com.corp.proyectoenoturismorutasvino.domain.model.EnoturismoRutasVino;
import com.corp.proyectoenoturismorutasvino.domain.port.in.ManageEnoturismoRutasVinoUseCase;
import com.corp.proyectoenoturismorutasvino.domain.port.out.EnoturismoRutasVinoRepositoryPort;
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
public class EnoturismoRutasVinoApplicationService implements ManageEnoturismoRutasVinoUseCase {

    private final EnoturismoRutasVinoRepositoryPort repositoryPort;

    public EnoturismoRutasVinoApplicationService(EnoturismoRutasVinoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EnoturismoRutasVino createEnoturismoRutasVino(String tenantId, String title, double value) {
        EnoturismoRutasVino entity = new EnoturismoRutasVino(
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
    public Optional<EnoturismoRutasVino> findEnoturismoRutasVinoById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EnoturismoRutasVino processOptimization(String id, String tenantId) {
        EnoturismoRutasVino existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EnoturismoRutasVino optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
