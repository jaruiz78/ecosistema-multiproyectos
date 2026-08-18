package com.corp.proyectoaptamerdiagnosticbiosensors.application.service;

import com.corp.proyectoaptamerdiagnosticbiosensors.domain.model.AptamerDissociationConstantKdToken;
import com.corp.proyectoaptamerdiagnosticbiosensors.domain.port.in.ManageAptamerDissociationConstantKdTokenUseCase;
import com.corp.proyectoaptamerdiagnosticbiosensors.domain.port.out.AptamerDissociationConstantKdTokenRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de AptamerDissociationConstantKdToken.
 */
@Service
public class AptamerDissociationConstantKdTokenApplicationService implements ManageAptamerDissociationConstantKdTokenUseCase {

    private final AptamerDissociationConstantKdTokenRepositoryPort repositoryPort;

    public AptamerDissociationConstantKdTokenApplicationService(AptamerDissociationConstantKdTokenRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public AptamerDissociationConstantKdToken createAptamerDissociationConstantKdToken(String tenantId, String title, double value) {
        AptamerDissociationConstantKdToken entity = new AptamerDissociationConstantKdToken(
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
    public Optional<AptamerDissociationConstantKdToken> findAptamerDissociationConstantKdTokenById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public AptamerDissociationConstantKdToken processOptimization(String id, String tenantId) {
        AptamerDissociationConstantKdToken existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        AptamerDissociationConstantKdToken optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
