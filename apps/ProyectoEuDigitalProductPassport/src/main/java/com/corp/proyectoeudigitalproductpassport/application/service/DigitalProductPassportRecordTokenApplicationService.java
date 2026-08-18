package com.corp.proyectoeudigitalproductpassport.application.service;

import com.corp.proyectoeudigitalproductpassport.domain.model.DigitalProductPassportRecordToken;
import com.corp.proyectoeudigitalproductpassport.domain.port.in.ManageDigitalProductPassportRecordTokenUseCase;
import com.corp.proyectoeudigitalproductpassport.domain.port.out.DigitalProductPassportRecordTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de DigitalProductPassportRecordToken.
 */
@Service
public class DigitalProductPassportRecordTokenApplicationService implements ManageDigitalProductPassportRecordTokenUseCase {

    private final DigitalProductPassportRecordTokenRepositoryPort repositoryPort;

    public DigitalProductPassportRecordTokenApplicationService(DigitalProductPassportRecordTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public DigitalProductPassportRecordToken createDigitalProductPassportRecordToken(String tenantId, String title, double value) {
        DigitalProductPassportRecordToken entity = new DigitalProductPassportRecordToken(
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
    public Optional<DigitalProductPassportRecordToken> findDigitalProductPassportRecordTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public DigitalProductPassportRecordToken processOptimization(String id, String tenantId) {
        DigitalProductPassportRecordToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        DigitalProductPassportRecordToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
