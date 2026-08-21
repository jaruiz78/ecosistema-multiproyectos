package com.corp.proyectoconfidentialdatacleanroom.application.service;

import com.corp.proyectoconfidentialdatacleanroom.domain.model.SecureEnclaveAnalyticsAttestationToken;
import com.corp.proyectoconfidentialdatacleanroom.domain.port.in.ManageSecureEnclaveAnalyticsAttestationTokenUseCase;
import com.corp.proyectoconfidentialdatacleanroom.domain.port.out.SecureEnclaveAnalyticsAttestationTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de SecureEnclaveAnalyticsAttestationToken.
 */
@Service
/**
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/formacion_ecosistema/UNIVERSIDAD_PRIVADA_ECOSISTEMA_CURRICULUM.md">Universidad Privada del Ecosistema</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-001-java25-virtual-threads-anti-pinning.md">ADR 001 Loom</a>
 */
public class SecureEnclaveAnalyticsAttestationTokenApplicationService implements ManageSecureEnclaveAnalyticsAttestationTokenUseCase {

    private final SecureEnclaveAnalyticsAttestationTokenRepositoryPort repositoryPort;

    public SecureEnclaveAnalyticsAttestationTokenApplicationService(SecureEnclaveAnalyticsAttestationTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public SecureEnclaveAnalyticsAttestationToken createSecureEnclaveAnalyticsAttestationToken(String tenantId, String title, double value) {
        SecureEnclaveAnalyticsAttestationToken entity = new SecureEnclaveAnalyticsAttestationToken(
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
    public Optional<SecureEnclaveAnalyticsAttestationToken> findSecureEnclaveAnalyticsAttestationTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public SecureEnclaveAnalyticsAttestationToken processOptimization(String id, String tenantId) {
        SecureEnclaveAnalyticsAttestationToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        SecureEnclaveAnalyticsAttestationToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
