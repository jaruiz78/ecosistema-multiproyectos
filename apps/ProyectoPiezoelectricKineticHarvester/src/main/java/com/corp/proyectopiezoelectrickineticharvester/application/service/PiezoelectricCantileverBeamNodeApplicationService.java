package com.corp.proyectopiezoelectrickineticharvester.application.service;

import com.corp.proyectopiezoelectrickineticharvester.domain.model.PiezoelectricCantileverBeamNode;
import com.corp.proyectopiezoelectrickineticharvester.domain.port.in.ManagePiezoelectricCantileverBeamNodeUseCase;
import com.corp.proyectopiezoelectrickineticharvester.domain.port.out.PiezoelectricCantileverBeamNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PiezoelectricCantileverBeamNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PiezoelectricCantileverBeamNodeApplicationService implements ManagePiezoelectricCantileverBeamNodeUseCase {

    private final PiezoelectricCantileverBeamNodeRepositoryPort repositoryPort;

    public PiezoelectricCantileverBeamNodeApplicationService(PiezoelectricCantileverBeamNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PiezoelectricCantileverBeamNode createPiezoelectricCantileverBeamNode(String tenantId, String title, double value) {
        PiezoelectricCantileverBeamNode entity = new PiezoelectricCantileverBeamNode(
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
    public Optional<PiezoelectricCantileverBeamNode> findPiezoelectricCantileverBeamNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PiezoelectricCantileverBeamNode processOptimization(String id, String tenantId) {
        PiezoelectricCantileverBeamNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PiezoelectricCantileverBeamNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
