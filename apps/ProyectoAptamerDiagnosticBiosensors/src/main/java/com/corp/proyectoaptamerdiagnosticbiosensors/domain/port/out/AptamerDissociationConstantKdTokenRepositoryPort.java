package com.corp.proyectoaptamerdiagnosticbiosensors.domain.port.out;

import com.corp.proyectoaptamerdiagnosticbiosensors.domain.model.AptamerDissociationConstantKdToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AptamerDissociationConstantKdTokenRepositoryPort {
    AptamerDissociationConstantKdToken save(AptamerDissociationConstantKdToken entity);
    Optional<AptamerDissociationConstantKdToken> findById(String id, String tenantId);
}
