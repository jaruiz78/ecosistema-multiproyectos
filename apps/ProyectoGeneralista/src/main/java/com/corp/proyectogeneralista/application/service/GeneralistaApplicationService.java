package com.corp.proyectogeneralista.application.service;

import com.corp.proyectogeneralista.domain.model.Generalista;
import com.corp.proyectogeneralista.domain.port.in.ManageGeneralistaUseCase;
import com.corp.proyectogeneralista.domain.port.out.GeneralistaRepositoryPort;
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
public class GeneralistaApplicationService implements ManageGeneralistaUseCase {

    private final GeneralistaRepositoryPort repositoryPort;

    public GeneralistaApplicationService(GeneralistaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Generalista createGeneralista(String tenantId, String title, double value) {
        Generalista entity = new Generalista(
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
    public Optional<Generalista> findGeneralistaById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Generalista processOptimization(String id, String tenantId) {
        Generalista existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Generalista optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
