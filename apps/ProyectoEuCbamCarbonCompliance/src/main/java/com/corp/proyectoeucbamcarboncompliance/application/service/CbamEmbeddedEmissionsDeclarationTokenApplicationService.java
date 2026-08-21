package com.corp.proyectoeucbamcarboncompliance.application.service;

import com.corp.proyectoeucbamcarboncompliance.domain.model.CbamEmbeddedEmissionsDeclarationToken;
import com.corp.proyectoeucbamcarboncompliance.domain.port.in.ManageCbamEmbeddedEmissionsDeclarationTokenUseCase;
import com.corp.proyectoeucbamcarboncompliance.domain.port.out.CbamEmbeddedEmissionsDeclarationTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CbamEmbeddedEmissionsDeclarationToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class CbamEmbeddedEmissionsDeclarationTokenApplicationService implements ManageCbamEmbeddedEmissionsDeclarationTokenUseCase {

    private final CbamEmbeddedEmissionsDeclarationTokenRepositoryPort repositoryPort;

    public CbamEmbeddedEmissionsDeclarationTokenApplicationService(CbamEmbeddedEmissionsDeclarationTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CbamEmbeddedEmissionsDeclarationToken createCbamEmbeddedEmissionsDeclarationToken(String tenantId, String title, double value) {
        CbamEmbeddedEmissionsDeclarationToken entity = new CbamEmbeddedEmissionsDeclarationToken(
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
    public Optional<CbamEmbeddedEmissionsDeclarationToken> findCbamEmbeddedEmissionsDeclarationTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CbamEmbeddedEmissionsDeclarationToken processOptimization(String id, String tenantId) {
        CbamEmbeddedEmissionsDeclarationToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CbamEmbeddedEmissionsDeclarationToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
