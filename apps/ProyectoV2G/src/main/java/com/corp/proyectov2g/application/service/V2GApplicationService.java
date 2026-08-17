package com.corp.proyectov2g.application.service;

import com.corp.proyectov2g.domain.model.V2G;
import com.corp.proyectov2g.domain.port.in.ManageV2GUseCase;
import com.corp.proyectov2g.domain.port.out.V2GRepositoryPort;
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
public class V2GApplicationService implements ManageV2GUseCase {

    private final V2GRepositoryPort repositoryPort;

    public V2GApplicationService(V2GRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public V2G createV2G(String tenantId, String title, double value) {
        V2G entity = new V2G(
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
    public Optional<V2G> findV2GById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public V2G processOptimization(String id, String tenantId) {
        V2G existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        V2G optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
