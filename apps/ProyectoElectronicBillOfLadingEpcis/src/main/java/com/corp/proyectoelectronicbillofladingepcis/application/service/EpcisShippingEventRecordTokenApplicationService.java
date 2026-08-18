package com.corp.proyectoelectronicbillofladingepcis.application.service;

import com.corp.proyectoelectronicbillofladingepcis.domain.model.EpcisShippingEventRecordToken;
import com.corp.proyectoelectronicbillofladingepcis.domain.port.in.ManageEpcisShippingEventRecordTokenUseCase;
import com.corp.proyectoelectronicbillofladingepcis.domain.port.out.EpcisShippingEventRecordTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de EpcisShippingEventRecordToken.
 */
@Service
public class EpcisShippingEventRecordTokenApplicationService implements ManageEpcisShippingEventRecordTokenUseCase {

    private final EpcisShippingEventRecordTokenRepositoryPort repositoryPort;

    public EpcisShippingEventRecordTokenApplicationService(EpcisShippingEventRecordTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public EpcisShippingEventRecordToken createEpcisShippingEventRecordToken(String tenantId, String title, double value) {
        EpcisShippingEventRecordToken entity = new EpcisShippingEventRecordToken(
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
    public Optional<EpcisShippingEventRecordToken> findEpcisShippingEventRecordTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public EpcisShippingEventRecordToken processOptimization(String id, String tenantId) {
        EpcisShippingEventRecordToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        EpcisShippingEventRecordToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
