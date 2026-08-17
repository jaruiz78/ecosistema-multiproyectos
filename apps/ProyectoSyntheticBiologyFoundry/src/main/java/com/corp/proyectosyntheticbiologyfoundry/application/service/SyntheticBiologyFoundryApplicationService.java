package com.corp.proyectosyntheticbiologyfoundry.application.service;

import com.corp.proyectosyntheticbiologyfoundry.domain.model.SyntheticBiologyFoundry;
import com.corp.proyectosyntheticbiologyfoundry.domain.port.in.ManageSyntheticBiologyFoundryUseCase;
import com.corp.proyectosyntheticbiologyfoundry.domain.port.out.SyntheticBiologyFoundryRepositoryPort;
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
public class SyntheticBiologyFoundryApplicationService implements ManageSyntheticBiologyFoundryUseCase {

    private final SyntheticBiologyFoundryRepositoryPort repositoryPort;

    public SyntheticBiologyFoundryApplicationService(SyntheticBiologyFoundryRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SyntheticBiologyFoundry createSyntheticBiologyFoundry(String tenantId, String title, double value) {
        SyntheticBiologyFoundry entity = new SyntheticBiologyFoundry(
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
    public Optional<SyntheticBiologyFoundry> findSyntheticBiologyFoundryById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SyntheticBiologyFoundry processOptimization(String id, String tenantId) {
        SyntheticBiologyFoundry existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SyntheticBiologyFoundry optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
