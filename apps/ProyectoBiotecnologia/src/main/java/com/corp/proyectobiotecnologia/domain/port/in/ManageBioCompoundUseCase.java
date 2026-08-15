package com.corp.proyectobiotecnologia.domain.port.in;

import com.corp.proyectobiotecnologia.domain.model.BioCompound;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBioCompoundUseCase {
    BioCompound createBioCompound(String tenantId, String title, double value);
    Optional<BioCompound> findBioCompoundById(String id, String tenantId);
    BioCompound processOptimization(String id, String tenantId);
}
