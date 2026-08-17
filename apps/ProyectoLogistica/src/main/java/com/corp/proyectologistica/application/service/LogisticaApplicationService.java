package com.corp.proyectologistica.application.service;

import com.corp.proyectologistica.domain.model.Logistica;
import com.corp.proyectologistica.domain.port.in.ManageLogisticaUseCase;
import com.corp.proyectologistica.domain.port.out.LogisticaRepositoryPort;
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
public class LogisticaApplicationService implements ManageLogisticaUseCase {

    private final LogisticaRepositoryPort repositoryPort;

    public LogisticaApplicationService(LogisticaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Logistica createLogistica(String tenantId, String title, double value) {
        Logistica entity = new Logistica(
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
    public Optional<Logistica> findLogisticaById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Logistica processOptimization(String id, String tenantId) {
        Logistica existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Logistica optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
