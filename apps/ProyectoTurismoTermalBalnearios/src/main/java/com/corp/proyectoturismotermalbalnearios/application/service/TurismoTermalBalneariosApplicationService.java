package com.corp.proyectoturismotermalbalnearios.application.service;

import com.corp.proyectoturismotermalbalnearios.domain.model.TurismoTermalBalnearios;
import com.corp.proyectoturismotermalbalnearios.domain.port.in.ManageTurismoTermalBalneariosUseCase;
import com.corp.proyectoturismotermalbalnearios.domain.port.out.TurismoTermalBalneariosRepositoryPort;
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
public class TurismoTermalBalneariosApplicationService implements ManageTurismoTermalBalneariosUseCase {

    private final TurismoTermalBalneariosRepositoryPort repositoryPort;

    public TurismoTermalBalneariosApplicationService(TurismoTermalBalneariosRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public TurismoTermalBalnearios createTurismoTermalBalnearios(String tenantId, String title, double value) {
        TurismoTermalBalnearios entity = new TurismoTermalBalnearios(
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
    public Optional<TurismoTermalBalnearios> findTurismoTermalBalneariosById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public TurismoTermalBalnearios processOptimization(String id, String tenantId) {
        TurismoTermalBalnearios existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        TurismoTermalBalnearios optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
