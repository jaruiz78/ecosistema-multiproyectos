package com.corp.proyectocartcelltherapeuticdesign.application.service;

import com.corp.proyectocartcelltherapeuticdesign.domain.model.CarTScfvBindingAffinityToken;
import com.corp.proyectocartcelltherapeuticdesign.domain.port.in.ManageCarTScfvBindingAffinityTokenUseCase;
import com.corp.proyectocartcelltherapeuticdesign.domain.port.out.CarTScfvBindingAffinityTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de CarTScfvBindingAffinityToken.
 */
@Service
public class CarTScfvBindingAffinityTokenApplicationService implements ManageCarTScfvBindingAffinityTokenUseCase {

    private final CarTScfvBindingAffinityTokenRepositoryPort repositoryPort;

    public CarTScfvBindingAffinityTokenApplicationService(CarTScfvBindingAffinityTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public CarTScfvBindingAffinityToken createCarTScfvBindingAffinityToken(String tenantId, String title, double value) {
        CarTScfvBindingAffinityToken entity = new CarTScfvBindingAffinityToken(
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
    public Optional<CarTScfvBindingAffinityToken> findCarTScfvBindingAffinityTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public CarTScfvBindingAffinityToken processOptimization(String id, String tenantId) {
        CarTScfvBindingAffinityToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        CarTScfvBindingAffinityToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
