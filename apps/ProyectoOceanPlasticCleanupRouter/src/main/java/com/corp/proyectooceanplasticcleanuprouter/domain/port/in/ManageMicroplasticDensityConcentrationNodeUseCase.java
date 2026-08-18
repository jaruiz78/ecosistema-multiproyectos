package com.corp.proyectooceanplasticcleanuprouter.domain.port.in;

import com.corp.proyectooceanplasticcleanuprouter.domain.model.MicroplasticDensityConcentrationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMicroplasticDensityConcentrationNodeUseCase {
    MicroplasticDensityConcentrationNode createMicroplasticDensityConcentrationNode(String tenantId, String title, double value);
    Optional<MicroplasticDensityConcentrationNode> findMicroplasticDensityConcentrationNodeById(String id, String tenantId);
    MicroplasticDensityConcentrationNode processOptimization(String id, String tenantId);
}
