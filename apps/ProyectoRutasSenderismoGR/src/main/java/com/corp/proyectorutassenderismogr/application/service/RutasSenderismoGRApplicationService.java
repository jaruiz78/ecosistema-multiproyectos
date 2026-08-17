package com.corp.proyectorutassenderismogr.application.service;

import com.corp.proyectorutassenderismogr.domain.model.RutasSenderismoGR;
import com.corp.proyectorutassenderismogr.domain.port.in.ManageRutasSenderismoGRUseCase;
import com.corp.proyectorutassenderismogr.domain.port.out.RutasSenderismoGRRepositoryPort;
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
public class RutasSenderismoGRApplicationService implements ManageRutasSenderismoGRUseCase {

    private final RutasSenderismoGRRepositoryPort repositoryPort;

    public RutasSenderismoGRApplicationService(RutasSenderismoGRRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public RutasSenderismoGR createRutasSenderismoGR(String tenantId, String title, double value) {
        RutasSenderismoGR entity = new RutasSenderismoGR(
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
    public Optional<RutasSenderismoGR> findRutasSenderismoGRById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public RutasSenderismoGR processOptimization(String id, String tenantId) {
        RutasSenderismoGR existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        RutasSenderismoGR optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
