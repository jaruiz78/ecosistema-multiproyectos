package com.corp.proyectopostquantumcertificateauthority.application.service;

import com.corp.proyectopostquantumcertificateauthority.domain.model.PqHybridX509CertificateToken;
import com.corp.proyectopostquantumcertificateauthority.domain.port.in.ManagePqHybridX509CertificateTokenUseCase;
import com.corp.proyectopostquantumcertificateauthority.domain.port.out.PqHybridX509CertificateTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de PqHybridX509CertificateToken.
 */
@Service
public class PqHybridX509CertificateTokenApplicationService implements ManagePqHybridX509CertificateTokenUseCase {

    private final PqHybridX509CertificateTokenRepositoryPort repositoryPort;

    public PqHybridX509CertificateTokenApplicationService(PqHybridX509CertificateTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public PqHybridX509CertificateToken createPqHybridX509CertificateToken(String tenantId, String title, double value) {
        PqHybridX509CertificateToken entity = new PqHybridX509CertificateToken(
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
    public Optional<PqHybridX509CertificateToken> findPqHybridX509CertificateTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public PqHybridX509CertificateToken processOptimization(String id, String tenantId) {
        PqHybridX509CertificateToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        PqHybridX509CertificateToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
