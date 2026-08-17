package com.corp.proyectoindustrialmicrogridmpc.application.service;

import com.corp.proyectoindustrialmicrogridmpc.domain.model.IndustrialMicrogridMPC;
import com.corp.proyectoindustrialmicrogridmpc.domain.port.in.ManageIndustrialMicrogridMPCUseCase;
import com.corp.proyectoindustrialmicrogridmpc.domain.port.out.IndustrialMicrogridMPCRepositoryPort;
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
public class IndustrialMicrogridMPCApplicationService implements ManageIndustrialMicrogridMPCUseCase {

    private final IndustrialMicrogridMPCRepositoryPort repositoryPort;

    public IndustrialMicrogridMPCApplicationService(IndustrialMicrogridMPCRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public IndustrialMicrogridMPC createIndustrialMicrogridMPC(String tenantId, String title, double value) {
        IndustrialMicrogridMPC entity = new IndustrialMicrogridMPC(
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
    public Optional<IndustrialMicrogridMPC> findIndustrialMicrogridMPCById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public IndustrialMicrogridMPC processOptimization(String id, String tenantId) {
        IndustrialMicrogridMPC existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        IndustrialMicrogridMPC optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
