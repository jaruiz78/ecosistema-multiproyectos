package com.corp.proyectohydrogenfuelcelllongrangedrone.application.service;

import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.model.PemFuelCellStackEfficiencyNode;
import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.port.in.ManagePemFuelCellStackEfficiencyNodeUseCase;
import com.corp.proyectohydrogenfuelcelllongrangedrone.domain.port.out.PemFuelCellStackEfficiencyNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PemFuelCellStackEfficiencyNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PemFuelCellStackEfficiencyNodeApplicationService implements ManagePemFuelCellStackEfficiencyNodeUseCase {

    private final PemFuelCellStackEfficiencyNodeRepositoryPort repositoryPort;

    public PemFuelCellStackEfficiencyNodeApplicationService(PemFuelCellStackEfficiencyNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PemFuelCellStackEfficiencyNode createPemFuelCellStackEfficiencyNode(String tenantId, String title, double value) {
        PemFuelCellStackEfficiencyNode entity = new PemFuelCellStackEfficiencyNode(
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
    public Optional<PemFuelCellStackEfficiencyNode> findPemFuelCellStackEfficiencyNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PemFuelCellStackEfficiencyNode processOptimization(String id, String tenantId) {
        PemFuelCellStackEfficiencyNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PemFuelCellStackEfficiencyNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
