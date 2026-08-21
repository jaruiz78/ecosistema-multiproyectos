package com.corp.proyectosubseafiberseismicmonitor.application.service;

import com.corp.proyectosubseafiberseismicmonitor.domain.model.FiberOpticRayleighAcousticNode;
import com.corp.proyectosubseafiberseismicmonitor.domain.port.in.ManageFiberOpticRayleighAcousticNodeUseCase;
import com.corp.proyectosubseafiberseismicmonitor.domain.port.out.FiberOpticRayleighAcousticNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de FiberOpticRayleighAcousticNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class FiberOpticRayleighAcousticNodeApplicationService implements ManageFiberOpticRayleighAcousticNodeUseCase {

    private final FiberOpticRayleighAcousticNodeRepositoryPort repositoryPort;

    public FiberOpticRayleighAcousticNodeApplicationService(FiberOpticRayleighAcousticNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public FiberOpticRayleighAcousticNode createFiberOpticRayleighAcousticNode(String tenantId, String title, double value) {
        FiberOpticRayleighAcousticNode entity = new FiberOpticRayleighAcousticNode(
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
    public Optional<FiberOpticRayleighAcousticNode> findFiberOpticRayleighAcousticNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public FiberOpticRayleighAcousticNode processOptimization(String id, String tenantId) {
        FiberOpticRayleighAcousticNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        FiberOpticRayleighAcousticNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
