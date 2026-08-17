package com.corp.proyectosegitturdtistandard.application.service;

import com.corp.proyectosegitturdtistandard.domain.model.SegitturDtiStandard;
import com.corp.proyectosegitturdtistandard.domain.port.in.ManageSegitturDtiStandardUseCase;
import com.corp.proyectosegitturdtistandard.domain.port.out.SegitturDtiStandardRepositoryPort;
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
public class SegitturDtiStandardApplicationService implements ManageSegitturDtiStandardUseCase {

    private final SegitturDtiStandardRepositoryPort repositoryPort;

    public SegitturDtiStandardApplicationService(SegitturDtiStandardRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SegitturDtiStandard createSegitturDtiStandard(String tenantId, String title, double value) {
        SegitturDtiStandard entity = new SegitturDtiStandard(
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
    public Optional<SegitturDtiStandard> findSegitturDtiStandardById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SegitturDtiStandard processOptimization(String id, String tenantId) {
        SegitturDtiStandard existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SegitturDtiStandard optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
