package com.corp.proyectooceanplasticcleanuprouter.application.service;

import com.corp.proyectooceanplasticcleanuprouter.domain.model.MicroplasticDensityConcentrationNode;
import com.corp.proyectooceanplasticcleanuprouter.domain.port.in.ManageMicroplasticDensityConcentrationNodeUseCase;
import com.corp.proyectooceanplasticcleanuprouter.domain.port.out.MicroplasticDensityConcentrationNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MicroplasticDensityConcentrationNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class MicroplasticDensityConcentrationNodeApplicationService implements ManageMicroplasticDensityConcentrationNodeUseCase {

    private final MicroplasticDensityConcentrationNodeRepositoryPort repositoryPort;

    public MicroplasticDensityConcentrationNodeApplicationService(MicroplasticDensityConcentrationNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MicroplasticDensityConcentrationNode createMicroplasticDensityConcentrationNode(String tenantId, String title, double value) {
        MicroplasticDensityConcentrationNode entity = new MicroplasticDensityConcentrationNode(
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
    public Optional<MicroplasticDensityConcentrationNode> findMicroplasticDensityConcentrationNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MicroplasticDensityConcentrationNode processOptimization(String id, String tenantId) {
        MicroplasticDensityConcentrationNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MicroplasticDensityConcentrationNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
