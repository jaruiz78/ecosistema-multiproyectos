package com.corp.proyectoecotourismpassport.application.service;

import com.corp.proyectoecotourismpassport.domain.model.EcoTourismPassport;
import com.corp.proyectoecotourismpassport.domain.port.in.ManageEcoTourismPassportUseCase;
import com.corp.proyectoecotourismpassport.domain.port.out.EcoTourismPassportRepositoryPort;
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
public class EcoTourismPassportApplicationService implements ManageEcoTourismPassportUseCase {

    private final EcoTourismPassportRepositoryPort repositoryPort;

    public EcoTourismPassportApplicationService(EcoTourismPassportRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EcoTourismPassport createEcoTourismPassport(String tenantId, String title, double value) {
        EcoTourismPassport entity = new EcoTourismPassport(
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
    public Optional<EcoTourismPassport> findEcoTourismPassportById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EcoTourismPassport processOptimization(String id, String tenantId) {
        EcoTourismPassport existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EcoTourismPassport optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
