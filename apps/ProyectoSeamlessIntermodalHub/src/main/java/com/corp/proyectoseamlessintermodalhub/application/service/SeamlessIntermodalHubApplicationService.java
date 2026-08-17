package com.corp.proyectoseamlessintermodalhub.application.service;

import com.corp.proyectoseamlessintermodalhub.domain.model.SeamlessIntermodalHub;
import com.corp.proyectoseamlessintermodalhub.domain.port.in.ManageSeamlessIntermodalHubUseCase;
import com.corp.proyectoseamlessintermodalhub.domain.port.out.SeamlessIntermodalHubRepositoryPort;
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
public class SeamlessIntermodalHubApplicationService implements ManageSeamlessIntermodalHubUseCase {

    private final SeamlessIntermodalHubRepositoryPort repositoryPort;

    public SeamlessIntermodalHubApplicationService(SeamlessIntermodalHubRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SeamlessIntermodalHub createSeamlessIntermodalHub(String tenantId, String title, double value) {
        SeamlessIntermodalHub entity = new SeamlessIntermodalHub(
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
    public Optional<SeamlessIntermodalHub> findSeamlessIntermodalHubById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SeamlessIntermodalHub processOptimization(String id, String tenantId) {
        SeamlessIntermodalHub existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SeamlessIntermodalHub optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
