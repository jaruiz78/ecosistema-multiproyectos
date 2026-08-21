package com.corp.proyectodiscretevariableqkdmesh.application.service;

import com.corp.proyectodiscretevariableqkdmesh.domain.model.DvQkdDecoyStateKeyStreamToken;
import com.corp.proyectodiscretevariableqkdmesh.domain.port.in.ManageDvQkdDecoyStateKeyStreamTokenUseCase;
import com.corp.proyectodiscretevariableqkdmesh.domain.port.out.DvQkdDecoyStateKeyStreamTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DvQkdDecoyStateKeyStreamToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class DvQkdDecoyStateKeyStreamTokenApplicationService implements ManageDvQkdDecoyStateKeyStreamTokenUseCase {

    private final DvQkdDecoyStateKeyStreamTokenRepositoryPort repositoryPort;

    public DvQkdDecoyStateKeyStreamTokenApplicationService(DvQkdDecoyStateKeyStreamTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DvQkdDecoyStateKeyStreamToken createDvQkdDecoyStateKeyStreamToken(String tenantId, String title, double value) {
        DvQkdDecoyStateKeyStreamToken entity = new DvQkdDecoyStateKeyStreamToken(
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
    public Optional<DvQkdDecoyStateKeyStreamToken> findDvQkdDecoyStateKeyStreamTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DvQkdDecoyStateKeyStreamToken processOptimization(String id, String tenantId) {
        DvQkdDecoyStateKeyStreamToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DvQkdDecoyStateKeyStreamToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
