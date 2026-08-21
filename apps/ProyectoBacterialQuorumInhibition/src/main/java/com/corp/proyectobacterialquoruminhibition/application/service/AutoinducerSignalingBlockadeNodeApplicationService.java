package com.corp.proyectobacterialquoruminhibition.application.service;

import com.corp.proyectobacterialquoruminhibition.domain.model.AutoinducerSignalingBlockadeNode;
import com.corp.proyectobacterialquoruminhibition.domain.port.in.ManageAutoinducerSignalingBlockadeNodeUseCase;
import com.corp.proyectobacterialquoruminhibition.domain.port.out.AutoinducerSignalingBlockadeNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AutoinducerSignalingBlockadeNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AutoinducerSignalingBlockadeNodeApplicationService implements ManageAutoinducerSignalingBlockadeNodeUseCase {

    private final AutoinducerSignalingBlockadeNodeRepositoryPort repositoryPort;

    public AutoinducerSignalingBlockadeNodeApplicationService(AutoinducerSignalingBlockadeNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AutoinducerSignalingBlockadeNode createAutoinducerSignalingBlockadeNode(String tenantId, String title, double value) {
        AutoinducerSignalingBlockadeNode entity = new AutoinducerSignalingBlockadeNode(
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
    public Optional<AutoinducerSignalingBlockadeNode> findAutoinducerSignalingBlockadeNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AutoinducerSignalingBlockadeNode processOptimization(String id, String tenantId) {
        AutoinducerSignalingBlockadeNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AutoinducerSignalingBlockadeNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
