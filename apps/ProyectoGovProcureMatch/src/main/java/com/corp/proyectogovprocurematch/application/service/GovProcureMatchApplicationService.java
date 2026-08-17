package com.corp.proyectogovprocurematch.application.service;

import com.corp.proyectogovprocurematch.domain.model.GovProcureMatch;
import com.corp.proyectogovprocurematch.domain.port.in.ManageGovProcureMatchUseCase;
import com.corp.proyectogovprocurematch.domain.port.out.GovProcureMatchRepositoryPort;
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
public class GovProcureMatchApplicationService implements ManageGovProcureMatchUseCase {

    private final GovProcureMatchRepositoryPort repositoryPort;

    public GovProcureMatchApplicationService(GovProcureMatchRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GovProcureMatch createGovProcureMatch(String tenantId, String title, double value) {
        GovProcureMatch entity = new GovProcureMatch(
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
    public Optional<GovProcureMatch> findGovProcureMatchById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GovProcureMatch processOptimization(String id, String tenantId) {
        GovProcureMatch existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GovProcureMatch optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
