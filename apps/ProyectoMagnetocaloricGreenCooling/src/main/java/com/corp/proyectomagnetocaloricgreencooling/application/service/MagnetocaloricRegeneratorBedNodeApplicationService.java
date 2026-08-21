package com.corp.proyectomagnetocaloricgreencooling.application.service;

import com.corp.proyectomagnetocaloricgreencooling.domain.model.MagnetocaloricRegeneratorBedNode;
import com.corp.proyectomagnetocaloricgreencooling.domain.port.in.ManageMagnetocaloricRegeneratorBedNodeUseCase;
import com.corp.proyectomagnetocaloricgreencooling.domain.port.out.MagnetocaloricRegeneratorBedNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MagnetocaloricRegeneratorBedNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MagnetocaloricRegeneratorBedNodeApplicationService implements ManageMagnetocaloricRegeneratorBedNodeUseCase {

    private final MagnetocaloricRegeneratorBedNodeRepositoryPort repositoryPort;

    public MagnetocaloricRegeneratorBedNodeApplicationService(MagnetocaloricRegeneratorBedNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MagnetocaloricRegeneratorBedNode createMagnetocaloricRegeneratorBedNode(String tenantId, String title, double value) {
        MagnetocaloricRegeneratorBedNode entity = new MagnetocaloricRegeneratorBedNode(
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
    public Optional<MagnetocaloricRegeneratorBedNode> findMagnetocaloricRegeneratorBedNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MagnetocaloricRegeneratorBedNode processOptimization(String id, String tenantId) {
        MagnetocaloricRegeneratorBedNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MagnetocaloricRegeneratorBedNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
