package com.corp.proyectofleetcoldchain.application.service;

import com.corp.proyectofleetcoldchain.domain.model.FleetColdChain;
import com.corp.proyectofleetcoldchain.domain.port.in.ManageFleetColdChainUseCase;
import com.corp.proyectofleetcoldchain.domain.port.out.FleetColdChainRepositoryPort;
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
public class FleetColdChainApplicationService implements ManageFleetColdChainUseCase {

    private final FleetColdChainRepositoryPort repositoryPort;

    public FleetColdChainApplicationService(FleetColdChainRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public FleetColdChain createFleetColdChain(String tenantId, String title, double value) {
        FleetColdChain entity = new FleetColdChain(
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
    public Optional<FleetColdChain> findFleetColdChainById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public FleetColdChain processOptimization(String id, String tenantId) {
        FleetColdChain existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        FleetColdChain optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
