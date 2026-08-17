package com.corp.proyectodualairdefense.application.service;

import com.corp.proyectodualairdefense.domain.model.DualAirDefense;
import com.corp.proyectodualairdefense.domain.port.in.ManageDualAirDefenseUseCase;
import com.corp.proyectodualairdefense.domain.port.out.DualAirDefenseRepositoryPort;
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
public class DualAirDefenseApplicationService implements ManageDualAirDefenseUseCase {

    private final DualAirDefenseRepositoryPort repositoryPort;

    public DualAirDefenseApplicationService(DualAirDefenseRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DualAirDefense createDualAirDefense(String tenantId, String title, double value) {
        DualAirDefense entity = new DualAirDefense(
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
    public Optional<DualAirDefense> findDualAirDefenseById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DualAirDefense processOptimization(String id, String tenantId) {
        DualAirDefense existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DualAirDefense optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
