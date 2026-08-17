package com.corp.proyectodiputacionturismorural.application.service;

import com.corp.proyectodiputacionturismorural.domain.model.DiputacionTurismoRural;
import com.corp.proyectodiputacionturismorural.domain.port.in.ManageDiputacionTurismoRuralUseCase;
import com.corp.proyectodiputacionturismorural.domain.port.out.DiputacionTurismoRuralRepositoryPort;
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
public class DiputacionTurismoRuralApplicationService implements ManageDiputacionTurismoRuralUseCase {

    private final DiputacionTurismoRuralRepositoryPort repositoryPort;

    public DiputacionTurismoRuralApplicationService(DiputacionTurismoRuralRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DiputacionTurismoRural createDiputacionTurismoRural(String tenantId, String title, double value) {
        DiputacionTurismoRural entity = new DiputacionTurismoRural(
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
    public Optional<DiputacionTurismoRural> findDiputacionTurismoRuralById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DiputacionTurismoRural processOptimization(String id, String tenantId) {
        DiputacionTurismoRural existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DiputacionTurismoRural optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
