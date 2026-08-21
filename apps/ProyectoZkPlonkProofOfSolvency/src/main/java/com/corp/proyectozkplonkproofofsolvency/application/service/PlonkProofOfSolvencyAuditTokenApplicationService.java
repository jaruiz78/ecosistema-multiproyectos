package com.corp.proyectozkplonkproofofsolvency.application.service;

import com.corp.proyectozkplonkproofofsolvency.domain.model.PlonkProofOfSolvencyAuditToken;
import com.corp.proyectozkplonkproofofsolvency.domain.port.in.ManagePlonkProofOfSolvencyAuditTokenUseCase;
import com.corp.proyectozkplonkproofofsolvency.domain.port.out.PlonkProofOfSolvencyAuditTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PlonkProofOfSolvencyAuditToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class PlonkProofOfSolvencyAuditTokenApplicationService implements ManagePlonkProofOfSolvencyAuditTokenUseCase {

    private final PlonkProofOfSolvencyAuditTokenRepositoryPort repositoryPort;

    public PlonkProofOfSolvencyAuditTokenApplicationService(PlonkProofOfSolvencyAuditTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PlonkProofOfSolvencyAuditToken createPlonkProofOfSolvencyAuditToken(String tenantId, String title, double value) {
        PlonkProofOfSolvencyAuditToken entity = new PlonkProofOfSolvencyAuditToken(
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
    public Optional<PlonkProofOfSolvencyAuditToken> findPlonkProofOfSolvencyAuditTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PlonkProofOfSolvencyAuditToken processOptimization(String id, String tenantId) {
        PlonkProofOfSolvencyAuditToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PlonkProofOfSolvencyAuditToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
