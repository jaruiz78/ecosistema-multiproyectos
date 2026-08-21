package com.corp.proyectospacedebrislasermitigation.application.service;

import com.corp.proyectospacedebrislasermitigation.domain.model.SpaceDebrisConjunctionTrackToken;
import com.corp.proyectospacedebrislasermitigation.domain.port.in.ManageSpaceDebrisConjunctionTrackTokenUseCase;
import com.corp.proyectospacedebrislasermitigation.domain.port.out.SpaceDebrisConjunctionTrackTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SpaceDebrisConjunctionTrackToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SpaceDebrisConjunctionTrackTokenApplicationService implements ManageSpaceDebrisConjunctionTrackTokenUseCase {

    private final SpaceDebrisConjunctionTrackTokenRepositoryPort repositoryPort;

    public SpaceDebrisConjunctionTrackTokenApplicationService(SpaceDebrisConjunctionTrackTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SpaceDebrisConjunctionTrackToken createSpaceDebrisConjunctionTrackToken(String tenantId, String title, double value) {
        SpaceDebrisConjunctionTrackToken entity = new SpaceDebrisConjunctionTrackToken(
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
    public Optional<SpaceDebrisConjunctionTrackToken> findSpaceDebrisConjunctionTrackTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SpaceDebrisConjunctionTrackToken processOptimization(String id, String tenantId) {
        SpaceDebrisConjunctionTrackToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SpaceDebrisConjunctionTrackToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
