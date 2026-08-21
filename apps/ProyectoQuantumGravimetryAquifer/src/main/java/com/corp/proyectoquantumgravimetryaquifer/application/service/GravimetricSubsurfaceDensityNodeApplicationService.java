package com.corp.proyectoquantumgravimetryaquifer.application.service;

import com.corp.proyectoquantumgravimetryaquifer.domain.model.GravimetricSubsurfaceDensityNode;
import com.corp.proyectoquantumgravimetryaquifer.domain.port.in.ManageGravimetricSubsurfaceDensityNodeUseCase;
import com.corp.proyectoquantumgravimetryaquifer.domain.port.out.GravimetricSubsurfaceDensityNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GravimetricSubsurfaceDensityNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GravimetricSubsurfaceDensityNodeApplicationService implements ManageGravimetricSubsurfaceDensityNodeUseCase {

    private final GravimetricSubsurfaceDensityNodeRepositoryPort repositoryPort;

    public GravimetricSubsurfaceDensityNodeApplicationService(GravimetricSubsurfaceDensityNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GravimetricSubsurfaceDensityNode createGravimetricSubsurfaceDensityNode(String tenantId, String title, double value) {
        GravimetricSubsurfaceDensityNode entity = new GravimetricSubsurfaceDensityNode(
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
    public Optional<GravimetricSubsurfaceDensityNode> findGravimetricSubsurfaceDensityNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GravimetricSubsurfaceDensityNode processOptimization(String id, String tenantId) {
        GravimetricSubsurfaceDensityNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GravimetricSubsurfaceDensityNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
