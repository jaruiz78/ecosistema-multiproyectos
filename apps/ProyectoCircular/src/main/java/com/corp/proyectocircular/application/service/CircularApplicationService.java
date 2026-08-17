package com.corp.proyectocircular.application.service;

import com.corp.proyectocircular.domain.model.Circular;
import com.corp.proyectocircular.domain.port.in.ManageCircularUseCase;
import com.corp.proyectocircular.domain.port.out.CircularRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/modulo_7_gestion_operaciones_logistica_ergonomia">FACULTAD_VIII: Ingeniería Industrial, Colas & Ergonomía</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom Anti-Pinning</a>
 */
public class CircularApplicationService implements ManageCircularUseCase {

    private final CircularRepositoryPort repositoryPort;

    public CircularApplicationService(CircularRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Circular createCircular(String tenantId, String title, double value) {
        Circular entity = new Circular(
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
    public Optional<Circular> findCircularById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Circular processOptimization(String id, String tenantId) {
        Circular existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Circular optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
