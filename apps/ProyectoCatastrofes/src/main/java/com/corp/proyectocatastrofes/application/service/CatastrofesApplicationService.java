package com.corp.proyectocatastrofes.application.service;

import com.corp.proyectocatastrofes.domain.model.Catastrofes;
import com.corp.proyectocatastrofes.domain.port.in.ManageCatastrofesUseCase;
import com.corp.proyectocatastrofes.domain.port.out.CatastrofesRepositoryPort;
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
public class CatastrofesApplicationService implements ManageCatastrofesUseCase {

    private final CatastrofesRepositoryPort repositoryPort;

    public CatastrofesApplicationService(CatastrofesRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public Catastrofes createCatastrofes(String tenantId, String title, double value) {
        Catastrofes entity = new Catastrofes(
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
    public Optional<Catastrofes> findCatastrofesById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public Catastrofes processOptimization(String id, String tenantId) {
        Catastrofes existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        Catastrofes optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
