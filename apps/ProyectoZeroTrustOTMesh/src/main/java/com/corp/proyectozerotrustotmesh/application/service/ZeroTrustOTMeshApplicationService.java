package com.corp.proyectozerotrustotmesh.application.service;

import com.corp.proyectozerotrustotmesh.domain.model.ZeroTrustOTMesh;
import com.corp.proyectozerotrustotmesh.domain.port.in.ManageZeroTrustOTMeshUseCase;
import com.corp.proyectozerotrustotmesh.domain.port.out.ZeroTrustOTMeshRepositoryPort;
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
public class ZeroTrustOTMeshApplicationService implements ManageZeroTrustOTMeshUseCase {

    private final ZeroTrustOTMeshRepositoryPort repositoryPort;

    public ZeroTrustOTMeshApplicationService(ZeroTrustOTMeshRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ZeroTrustOTMesh createZeroTrustOTMesh(String tenantId, String title, double value) {
        ZeroTrustOTMesh entity = new ZeroTrustOTMesh(
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
    public Optional<ZeroTrustOTMesh> findZeroTrustOTMeshById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public ZeroTrustOTMesh processOptimization(String id, String tenantId) {
        ZeroTrustOTMesh existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        ZeroTrustOTMesh optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
