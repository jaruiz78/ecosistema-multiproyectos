package com.corp.proyectoionpropulsionorbitalmaneuver.application.service;

import com.corp.proyectoionpropulsionorbitalmaneuver.domain.model.IonThrusterManeuverPlanToken;
import com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.in.ManageIonThrusterManeuverPlanTokenUseCase;
import com.corp.proyectoionpropulsionorbitalmaneuver.domain.port.out.IonThrusterManeuverPlanTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de IonThrusterManeuverPlanToken.
 */
@Service
public class IonThrusterManeuverPlanTokenApplicationService implements ManageIonThrusterManeuverPlanTokenUseCase {

    private final IonThrusterManeuverPlanTokenRepositoryPort repositoryPort;

    public IonThrusterManeuverPlanTokenApplicationService(IonThrusterManeuverPlanTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public IonThrusterManeuverPlanToken createIonThrusterManeuverPlanToken(String tenantId, String title, double value) {
        IonThrusterManeuverPlanToken entity = new IonThrusterManeuverPlanToken(
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
    public Optional<IonThrusterManeuverPlanToken> findIonThrusterManeuverPlanTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public IonThrusterManeuverPlanToken processOptimization(String id, String tenantId) {
        IonThrusterManeuverPlanToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        IonThrusterManeuverPlanToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
