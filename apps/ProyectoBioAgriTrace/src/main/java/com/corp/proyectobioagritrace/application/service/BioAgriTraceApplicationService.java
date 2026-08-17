package com.corp.proyectobioagritrace.application.service;

import com.corp.proyectobioagritrace.domain.model.BioAgriTrace;
import com.corp.proyectobioagritrace.domain.port.in.ManageBioAgriTraceUseCase;
import com.corp.proyectobioagritrace.domain.port.out.BioAgriTraceRepositoryPort;
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
public class BioAgriTraceApplicationService implements ManageBioAgriTraceUseCase {

    private final BioAgriTraceRepositoryPort repositoryPort;

    public BioAgriTraceApplicationService(BioAgriTraceRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public BioAgriTrace createBioAgriTrace(String tenantId, String title, double value) {
        BioAgriTrace entity = new BioAgriTrace(
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
    public Optional<BioAgriTrace> findBioAgriTraceById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public BioAgriTrace processOptimization(String id, String tenantId) {
        BioAgriTrace existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        BioAgriTrace optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
