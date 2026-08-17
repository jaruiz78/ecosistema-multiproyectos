package com.corp.proyectomiceconferencetwin.application.service;

import com.corp.proyectomiceconferencetwin.domain.model.MiceConferenceTwin;
import com.corp.proyectomiceconferencetwin.domain.port.in.ManageMiceConferenceTwinUseCase;
import com.corp.proyectomiceconferencetwin.domain.port.out.MiceConferenceTwinRepositoryPort;
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
public class MiceConferenceTwinApplicationService implements ManageMiceConferenceTwinUseCase {

    private final MiceConferenceTwinRepositoryPort repositoryPort;

    public MiceConferenceTwinApplicationService(MiceConferenceTwinRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MiceConferenceTwin createMiceConferenceTwin(String tenantId, String title, double value) {
        MiceConferenceTwin entity = new MiceConferenceTwin(
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
    public Optional<MiceConferenceTwin> findMiceConferenceTwinById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MiceConferenceTwin processOptimization(String id, String tenantId) {
        MiceConferenceTwin existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MiceConferenceTwin optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
