package com.corp.proyectometabolicoptknockengineering.application.service;

import com.corp.proyectometabolicoptknockengineering.domain.model.GeneDeletionTargetVectorToken;
import com.corp.proyectometabolicoptknockengineering.domain.port.in.ManageGeneDeletionTargetVectorTokenUseCase;
import com.corp.proyectometabolicoptknockengineering.domain.port.out.GeneDeletionTargetVectorTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de GeneDeletionTargetVectorToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class GeneDeletionTargetVectorTokenApplicationService implements ManageGeneDeletionTargetVectorTokenUseCase {

    private final GeneDeletionTargetVectorTokenRepositoryPort repositoryPort;

    public GeneDeletionTargetVectorTokenApplicationService(GeneDeletionTargetVectorTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public GeneDeletionTargetVectorToken createGeneDeletionTargetVectorToken(String tenantId, String title, double value) {
        GeneDeletionTargetVectorToken entity = new GeneDeletionTargetVectorToken(
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
    public Optional<GeneDeletionTargetVectorToken> findGeneDeletionTargetVectorTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public GeneDeletionTargetVectorToken processOptimization(String id, String tenantId) {
        GeneDeletionTargetVectorToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        GeneDeletionTargetVectorToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
