package com.corp.proyectofusionplasmatokamaktwin.application.service;

import com.corp.proyectofusionplasmatokamaktwin.domain.model.PlasmaMhdMagneticFluxNode;
import com.corp.proyectofusionplasmatokamaktwin.domain.port.in.ManagePlasmaMhdMagneticFluxNodeUseCase;
import com.corp.proyectofusionplasmatokamaktwin.domain.port.out.PlasmaMhdMagneticFluxNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PlasmaMhdMagneticFluxNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlasmaMhdMagneticFluxNodeApplicationService implements ManagePlasmaMhdMagneticFluxNodeUseCase {

    private final PlasmaMhdMagneticFluxNodeRepositoryPort repositoryPort;

    public PlasmaMhdMagneticFluxNodeApplicationService(PlasmaMhdMagneticFluxNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PlasmaMhdMagneticFluxNode createPlasmaMhdMagneticFluxNode(String tenantId, String title, double value) {
        PlasmaMhdMagneticFluxNode entity = new PlasmaMhdMagneticFluxNode(
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
    public Optional<PlasmaMhdMagneticFluxNode> findPlasmaMhdMagneticFluxNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PlasmaMhdMagneticFluxNode processOptimization(String id, String tenantId) {
        PlasmaMhdMagneticFluxNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PlasmaMhdMagneticFluxNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
