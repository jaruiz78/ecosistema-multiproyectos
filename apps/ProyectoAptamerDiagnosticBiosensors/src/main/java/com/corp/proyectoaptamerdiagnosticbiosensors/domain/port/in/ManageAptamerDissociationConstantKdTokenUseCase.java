package com.corp.proyectoaptamerdiagnosticbiosensors.domain.port.in;

import com.corp.proyectoaptamerdiagnosticbiosensors.domain.model.AptamerDissociationConstantKdToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAptamerDissociationConstantKdTokenUseCase {
    AptamerDissociationConstantKdToken createAptamerDissociationConstantKdToken(String tenantId, String title, double value);
    Optional<AptamerDissociationConstantKdToken> findAptamerDissociationConstantKdTokenById(String id, String tenantId);
    AptamerDissociationConstantKdToken processOptimization(String id, String tenantId);
}
