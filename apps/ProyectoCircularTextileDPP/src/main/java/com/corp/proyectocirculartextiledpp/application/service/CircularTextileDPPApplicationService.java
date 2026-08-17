package com.corp.proyectocirculartextiledpp.application.service;

import com.corp.proyectocirculartextiledpp.domain.model.CircularTextileDPP;
import com.corp.proyectocirculartextiledpp.domain.port.in.ManageCircularTextileDPPUseCase;
import com.corp.proyectocirculartextiledpp.domain.port.out.CircularTextileDPPRepositoryPort;
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
public class CircularTextileDPPApplicationService implements ManageCircularTextileDPPUseCase {

    private final CircularTextileDPPRepositoryPort repositoryPort;

    public CircularTextileDPPApplicationService(CircularTextileDPPRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CircularTextileDPP createCircularTextileDPP(String tenantId, String title, double value) {
        CircularTextileDPP entity = new CircularTextileDPP(
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
    public Optional<CircularTextileDPP> findCircularTextileDPPById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CircularTextileDPP processOptimization(String id, String tenantId) {
        CircularTextileDPP existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CircularTextileDPP optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
