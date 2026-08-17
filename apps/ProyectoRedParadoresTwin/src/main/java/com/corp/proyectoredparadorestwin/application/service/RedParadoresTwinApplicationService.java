package com.corp.proyectoredparadorestwin.application.service;

import com.corp.proyectoredparadorestwin.domain.model.RedParadoresTwin;
import com.corp.proyectoredparadorestwin.domain.port.in.ManageRedParadoresTwinUseCase;
import com.corp.proyectoredparadorestwin.domain.port.out.RedParadoresTwinRepositoryPort;
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
public class RedParadoresTwinApplicationService implements ManageRedParadoresTwinUseCase {

    private final RedParadoresTwinRepositoryPort repositoryPort;

    public RedParadoresTwinApplicationService(RedParadoresTwinRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public RedParadoresTwin createRedParadoresTwin(String tenantId, String title, double value) {
        RedParadoresTwin entity = new RedParadoresTwin(
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
    public Optional<RedParadoresTwin> findRedParadoresTwinById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public RedParadoresTwin processOptimization(String id, String tenantId) {
        RedParadoresTwin existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        RedParadoresTwin optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
