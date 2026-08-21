package com.corp.proyectocircadianlightingsmartcity.application.service;

import com.corp.proyectocircadianlightingsmartcity.domain.model.CircadianLightingMelanopicNode;
import com.corp.proyectocircadianlightingsmartcity.domain.port.in.ManageCircadianLightingMelanopicNodeUseCase;
import com.corp.proyectocircadianlightingsmartcity.domain.port.out.CircadianLightingMelanopicNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CircadianLightingMelanopicNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CircadianLightingMelanopicNodeApplicationService implements ManageCircadianLightingMelanopicNodeUseCase {

    private final CircadianLightingMelanopicNodeRepositoryPort repositoryPort;

    public CircadianLightingMelanopicNodeApplicationService(CircadianLightingMelanopicNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CircadianLightingMelanopicNode createCircadianLightingMelanopicNode(String tenantId, String title, double value) {
        CircadianLightingMelanopicNode entity = new CircadianLightingMelanopicNode(
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
    public Optional<CircadianLightingMelanopicNode> findCircadianLightingMelanopicNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CircadianLightingMelanopicNode processOptimization(String id, String tenantId) {
        CircadianLightingMelanopicNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CircadianLightingMelanopicNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
