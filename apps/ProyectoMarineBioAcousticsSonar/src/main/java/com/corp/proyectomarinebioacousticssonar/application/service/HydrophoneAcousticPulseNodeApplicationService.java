package com.corp.proyectomarinebioacousticssonar.application.service;

import com.corp.proyectomarinebioacousticssonar.domain.model.HydrophoneAcousticPulseNode;
import com.corp.proyectomarinebioacousticssonar.domain.port.in.ManageHydrophoneAcousticPulseNodeUseCase;
import com.corp.proyectomarinebioacousticssonar.domain.port.out.HydrophoneAcousticPulseNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de HydrophoneAcousticPulseNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class HydrophoneAcousticPulseNodeApplicationService implements ManageHydrophoneAcousticPulseNodeUseCase {

    private final HydrophoneAcousticPulseNodeRepositoryPort repositoryPort;

    public HydrophoneAcousticPulseNodeApplicationService(HydrophoneAcousticPulseNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public HydrophoneAcousticPulseNode createHydrophoneAcousticPulseNode(String tenantId, String title, double value) {
        HydrophoneAcousticPulseNode entity = new HydrophoneAcousticPulseNode(
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
    public Optional<HydrophoneAcousticPulseNode> findHydrophoneAcousticPulseNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public HydrophoneAcousticPulseNode processOptimization(String id, String tenantId) {
        HydrophoneAcousticPulseNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        HydrophoneAcousticPulseNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
