package com.corp.proyectobluecarbonoceans.application.service;

import com.corp.proyectobluecarbonoceans.domain.model.MarinePosidoniaCarbonSink;
import com.corp.proyectobluecarbonoceans.domain.port.in.ManageMarinePosidoniaCarbonSinkUseCase;
import com.corp.proyectobluecarbonoceans.domain.port.out.MarinePosidoniaCarbonSinkRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de MarinePosidoniaCarbonSink.
 */
@Service
public class MarinePosidoniaCarbonSinkApplicationService implements ManageMarinePosidoniaCarbonSinkUseCase {

    private final MarinePosidoniaCarbonSinkRepositoryPort repositoryPort;

    public MarinePosidoniaCarbonSinkApplicationService(MarinePosidoniaCarbonSinkRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public MarinePosidoniaCarbonSink createMarinePosidoniaCarbonSink(String tenantId, String title, double value) {
        MarinePosidoniaCarbonSink entity = new MarinePosidoniaCarbonSink(
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
    public Optional<MarinePosidoniaCarbonSink> findMarinePosidoniaCarbonSinkById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public MarinePosidoniaCarbonSink processOptimization(String id, String tenantId) {
        MarinePosidoniaCarbonSink existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        MarinePosidoniaCarbonSink optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
