package com.corp.proyectosoilbiocarbontwin.application.service;

import com.corp.proyectosoilbiocarbontwin.domain.model.SoilBioCarbonTwin;
import com.corp.proyectosoilbiocarbontwin.domain.port.in.ManageSoilBioCarbonTwinUseCase;
import com.corp.proyectosoilbiocarbontwin.domain.port.out.SoilBioCarbonTwinRepositoryPort;
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
public class SoilBioCarbonTwinApplicationService implements ManageSoilBioCarbonTwinUseCase {

    private final SoilBioCarbonTwinRepositoryPort repositoryPort;

    public SoilBioCarbonTwinApplicationService(SoilBioCarbonTwinRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SoilBioCarbonTwin createSoilBioCarbonTwin(String tenantId, String title, double value) {
        SoilBioCarbonTwin entity = new SoilBioCarbonTwin(
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
    public Optional<SoilBioCarbonTwin> findSoilBioCarbonTwinById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SoilBioCarbonTwin processOptimization(String id, String tenantId) {
        SoilBioCarbonTwin existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SoilBioCarbonTwin optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
