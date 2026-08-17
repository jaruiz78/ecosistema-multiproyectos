package com.corp.proyectosmartstreetlightingv2g.application.service;

import com.corp.proyectosmartstreetlightingv2g.domain.model.SmartStreetLightingV2G;
import com.corp.proyectosmartstreetlightingv2g.domain.port.in.ManageSmartStreetLightingV2GUseCase;
import com.corp.proyectosmartstreetlightingv2g.domain.port.out.SmartStreetLightingV2GRepositoryPort;
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
public class SmartStreetLightingV2GApplicationService implements ManageSmartStreetLightingV2GUseCase {

    private final SmartStreetLightingV2GRepositoryPort repositoryPort;

    public SmartStreetLightingV2GApplicationService(SmartStreetLightingV2GRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SmartStreetLightingV2G createSmartStreetLightingV2G(String tenantId, String title, double value) {
        SmartStreetLightingV2G entity = new SmartStreetLightingV2G(
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
    public Optional<SmartStreetLightingV2G> findSmartStreetLightingV2GById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SmartStreetLightingV2G processOptimization(String id, String tenantId) {
        SmartStreetLightingV2G existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SmartStreetLightingV2G optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
