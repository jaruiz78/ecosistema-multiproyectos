package com.corp.proyectoepigeneticbioagemonitor.application.service;

import com.corp.proyectoepigeneticbioagemonitor.domain.model.CpgMethylationProfileNode;
import com.corp.proyectoepigeneticbioagemonitor.domain.port.in.ManageCpgMethylationProfileNodeUseCase;
import com.corp.proyectoepigeneticbioagemonitor.domain.port.out.CpgMethylationProfileNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CpgMethylationProfileNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CpgMethylationProfileNodeApplicationService implements ManageCpgMethylationProfileNodeUseCase {

    private final CpgMethylationProfileNodeRepositoryPort repositoryPort;

    public CpgMethylationProfileNodeApplicationService(CpgMethylationProfileNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CpgMethylationProfileNode createCpgMethylationProfileNode(String tenantId, String title, double value) {
        CpgMethylationProfileNode entity = new CpgMethylationProfileNode(
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
    public Optional<CpgMethylationProfileNode> findCpgMethylationProfileNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CpgMethylationProfileNode processOptimization(String id, String tenantId) {
        CpgMethylationProfileNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CpgMethylationProfileNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
