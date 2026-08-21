package com.corp.proyectoterahertzmolecularscanner.application.service;

import com.corp.proyectoterahertzmolecularscanner.domain.model.TerahertzAbsorptionSpectrumNode;
import com.corp.proyectoterahertzmolecularscanner.domain.port.in.ManageTerahertzAbsorptionSpectrumNodeUseCase;
import com.corp.proyectoterahertzmolecularscanner.domain.port.out.TerahertzAbsorptionSpectrumNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de TerahertzAbsorptionSpectrumNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class TerahertzAbsorptionSpectrumNodeApplicationService implements ManageTerahertzAbsorptionSpectrumNodeUseCase {

    private final TerahertzAbsorptionSpectrumNodeRepositoryPort repositoryPort;

    public TerahertzAbsorptionSpectrumNodeApplicationService(TerahertzAbsorptionSpectrumNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TerahertzAbsorptionSpectrumNode createTerahertzAbsorptionSpectrumNode(String tenantId, String title, double value) {
        TerahertzAbsorptionSpectrumNode entity = new TerahertzAbsorptionSpectrumNode(
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
    public Optional<TerahertzAbsorptionSpectrumNode> findTerahertzAbsorptionSpectrumNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TerahertzAbsorptionSpectrumNode processOptimization(String id, String tenantId) {
        TerahertzAbsorptionSpectrumNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TerahertzAbsorptionSpectrumNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
