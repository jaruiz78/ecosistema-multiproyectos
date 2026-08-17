package com.corp.proyectopresatwinscada.application.service;

import com.corp.proyectopresatwinscada.domain.model.PresaTwinSCADA;
import com.corp.proyectopresatwinscada.domain.port.in.ManagePresaTwinSCADAUseCase;
import com.corp.proyectopresatwinscada.domain.port.out.PresaTwinSCADARepositoryPort;
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
public class PresaTwinSCADAApplicationService implements ManagePresaTwinSCADAUseCase {

    private final PresaTwinSCADARepositoryPort repositoryPort;

    public PresaTwinSCADAApplicationService(PresaTwinSCADARepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PresaTwinSCADA createPresaTwinSCADA(String tenantId, String title, double value) {
        PresaTwinSCADA entity = new PresaTwinSCADA(
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
    public Optional<PresaTwinSCADA> findPresaTwinSCADAById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PresaTwinSCADA processOptimization(String id, String tenantId) {
        PresaTwinSCADA existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PresaTwinSCADA optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
