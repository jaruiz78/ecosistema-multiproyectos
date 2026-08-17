package com.corp.proyectosmartwaterdesal.application.service;

import com.corp.proyectosmartwaterdesal.domain.model.SmartWaterDesal;
import com.corp.proyectosmartwaterdesal.domain.port.in.ManageSmartWaterDesalUseCase;
import com.corp.proyectosmartwaterdesal.domain.port.out.SmartWaterDesalRepositoryPort;
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
public class SmartWaterDesalApplicationService implements ManageSmartWaterDesalUseCase {

    private final SmartWaterDesalRepositoryPort repositoryPort;

    public SmartWaterDesalApplicationService(SmartWaterDesalRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SmartWaterDesal createSmartWaterDesal(String tenantId, String title, double value) {
        SmartWaterDesal entity = new SmartWaterDesal(
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
    public Optional<SmartWaterDesal> findSmartWaterDesalById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SmartWaterDesal processOptimization(String id, String tenantId) {
        SmartWaterDesal existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SmartWaterDesal optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
