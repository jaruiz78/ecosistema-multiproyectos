package com.corp.proyectophytoremediationsoilclean.application.service;

import com.corp.proyectophytoremediationsoilclean.domain.model.HeavyMetalBioaccumulationFactorNode;
import com.corp.proyectophytoremediationsoilclean.domain.port.in.ManageHeavyMetalBioaccumulationFactorNodeUseCase;
import com.corp.proyectophytoremediationsoilclean.domain.port.out.HeavyMetalBioaccumulationFactorNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HeavyMetalBioaccumulationFactorNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HeavyMetalBioaccumulationFactorNodeApplicationService implements ManageHeavyMetalBioaccumulationFactorNodeUseCase {

    private final HeavyMetalBioaccumulationFactorNodeRepositoryPort repositoryPort;

    public HeavyMetalBioaccumulationFactorNodeApplicationService(HeavyMetalBioaccumulationFactorNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HeavyMetalBioaccumulationFactorNode createHeavyMetalBioaccumulationFactorNode(String tenantId, String title, double value) {
        HeavyMetalBioaccumulationFactorNode entity = new HeavyMetalBioaccumulationFactorNode(
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
    public Optional<HeavyMetalBioaccumulationFactorNode> findHeavyMetalBioaccumulationFactorNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HeavyMetalBioaccumulationFactorNode processOptimization(String id, String tenantId) {
        HeavyMetalBioaccumulationFactorNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HeavyMetalBioaccumulationFactorNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
