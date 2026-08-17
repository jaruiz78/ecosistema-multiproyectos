package com.corp.proyectosmartagrisupplychain.application.service;

import com.corp.proyectosmartagrisupplychain.domain.model.AgriSupplyTrack;
import com.corp.proyectosmartagrisupplychain.domain.port.in.ManageAgriSupplyTrackUseCase;
import com.corp.proyectosmartagrisupplychain.domain.port.out.AgriSupplyTrackRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AgriSupplyTrack.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_1_java_spring_boot">FACULTAD_I: Software Engineering, DDD Puro & Tipos</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class AgriSupplyTrackApplicationService implements ManageAgriSupplyTrackUseCase {

    private final AgriSupplyTrackRepositoryPort repositoryPort;

    public AgriSupplyTrackApplicationService(AgriSupplyTrackRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AgriSupplyTrack createAgriSupplyTrack(String tenantId, String title, double value) {
        AgriSupplyTrack entity = new AgriSupplyTrack(
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
    public Optional<AgriSupplyTrack> findAgriSupplyTrackById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AgriSupplyTrack processOptimization(String id, String tenantId) {
        AgriSupplyTrack existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AgriSupplyTrack optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
