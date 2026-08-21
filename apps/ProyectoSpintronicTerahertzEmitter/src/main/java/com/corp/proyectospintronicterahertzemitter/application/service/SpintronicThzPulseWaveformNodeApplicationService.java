package com.corp.proyectospintronicterahertzemitter.application.service;

import com.corp.proyectospintronicterahertzemitter.domain.model.SpintronicThzPulseWaveformNode;
import com.corp.proyectospintronicterahertzemitter.domain.port.in.ManageSpintronicThzPulseWaveformNodeUseCase;
import com.corp.proyectospintronicterahertzemitter.domain.port.out.SpintronicThzPulseWaveformNodeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpintronicThzPulseWaveformNode.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpintronicThzPulseWaveformNodeApplicationService implements ManageSpintronicThzPulseWaveformNodeUseCase {

    private final SpintronicThzPulseWaveformNodeRepositoryPort repositoryPort;

    public SpintronicThzPulseWaveformNodeApplicationService(SpintronicThzPulseWaveformNodeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpintronicThzPulseWaveformNode createSpintronicThzPulseWaveformNode(String tenantId, String title, double value) {
        SpintronicThzPulseWaveformNode entity = new SpintronicThzPulseWaveformNode(
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
    public Optional<SpintronicThzPulseWaveformNode> findSpintronicThzPulseWaveformNodeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpintronicThzPulseWaveformNode processOptimization(String id, String tenantId) {
        SpintronicThzPulseWaveformNode existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpintronicThzPulseWaveformNode optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
