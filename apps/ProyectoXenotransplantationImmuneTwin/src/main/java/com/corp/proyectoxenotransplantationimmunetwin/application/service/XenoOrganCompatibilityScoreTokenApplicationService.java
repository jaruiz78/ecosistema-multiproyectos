package com.corp.proyectoxenotransplantationimmunetwin.application.service;

import com.corp.proyectoxenotransplantationimmunetwin.domain.model.XenoOrganCompatibilityScoreToken;
import com.corp.proyectoxenotransplantationimmunetwin.domain.port.in.ManageXenoOrganCompatibilityScoreTokenUseCase;
import com.corp.proyectoxenotransplantationimmunetwin.domain.port.out.XenoOrganCompatibilityScoreTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de XenoOrganCompatibilityScoreToken.
 */
@Service
public class XenoOrganCompatibilityScoreTokenApplicationService implements ManageXenoOrganCompatibilityScoreTokenUseCase {

    private final XenoOrganCompatibilityScoreTokenRepositoryPort repositoryPort;

    public XenoOrganCompatibilityScoreTokenApplicationService(XenoOrganCompatibilityScoreTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public XenoOrganCompatibilityScoreToken createXenoOrganCompatibilityScoreToken(String tenantId, String title, double value) {
        XenoOrganCompatibilityScoreToken entity = new XenoOrganCompatibilityScoreToken(
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
    public Optional<XenoOrganCompatibilityScoreToken> findXenoOrganCompatibilityScoreTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public XenoOrganCompatibilityScoreToken processOptimization(String id, String tenantId) {
        XenoOrganCompatibilityScoreToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        XenoOrganCompatibilityScoreToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
