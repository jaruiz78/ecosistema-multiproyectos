package com.corp.proyectostratosphericsai.application.service;

import com.corp.proyectostratosphericsai.domain.model.AerosolInjectionPlume;
import com.corp.proyectostratosphericsai.domain.port.in.ManageAerosolInjectionPlumeUseCase;
import com.corp.proyectostratosphericsai.domain.port.out.AerosolInjectionPlumeRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AerosolInjectionPlume.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class AerosolInjectionPlumeApplicationService implements ManageAerosolInjectionPlumeUseCase {

    private final AerosolInjectionPlumeRepositoryPort repositoryPort;

    public AerosolInjectionPlumeApplicationService(AerosolInjectionPlumeRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AerosolInjectionPlume createAerosolInjectionPlume(String tenantId, String title, double value) {
        AerosolInjectionPlume entity = new AerosolInjectionPlume(
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
    public Optional<AerosolInjectionPlume> findAerosolInjectionPlumeById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AerosolInjectionPlume processOptimization(String id, String tenantId) {
        AerosolInjectionPlume existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AerosolInjectionPlume optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
