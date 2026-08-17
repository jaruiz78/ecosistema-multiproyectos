package com.corp.proyectoagua.application.service;

import com.corp.proyectoagua.domain.model.Agua;
import com.corp.proyectoagua.domain.port.in.ManageAguaUseCase;
import com.corp.proyectoagua.domain.port.out.AguaRepositoryPort;
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
public class AguaApplicationService implements ManageAguaUseCase {

    private final AguaRepositoryPort repositoryPort;

    public AguaApplicationService(AguaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Agua createAgua(String tenantId, String title, double value) {
        Agua entity = new Agua(
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
    public Optional<Agua> findAguaById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Agua processOptimization(String id, String tenantId) {
        Agua existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Agua optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
