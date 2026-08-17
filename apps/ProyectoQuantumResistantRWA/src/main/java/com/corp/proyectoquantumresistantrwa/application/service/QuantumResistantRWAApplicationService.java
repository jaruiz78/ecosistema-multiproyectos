package com.corp.proyectoquantumresistantrwa.application.service;

import com.corp.proyectoquantumresistantrwa.domain.model.QuantumResistantRWA;
import com.corp.proyectoquantumresistantrwa.domain.port.in.ManageQuantumResistantRWAUseCase;
import com.corp.proyectoquantumresistantrwa.domain.port.out.QuantumResistantRWARepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class QuantumResistantRWAApplicationService implements ManageQuantumResistantRWAUseCase {

    private final QuantumResistantRWARepositoryPort repositoryPort;

    public QuantumResistantRWAApplicationService(QuantumResistantRWARepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public QuantumResistantRWA createQuantumResistantRWA(String tenantId, String title, double value) {
        QuantumResistantRWA entity = new QuantumResistantRWA(
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
    public Optional<QuantumResistantRWA> findQuantumResistantRWAById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public QuantumResistantRWA processOptimization(String id, String tenantId) {
        QuantumResistantRWA existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        QuantumResistantRWA optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
