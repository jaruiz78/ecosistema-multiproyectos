package com.corp.proyectooceanplasticcleanuprouter.domain.port.out;

import com.corp.proyectooceanplasticcleanuprouter.domain.model.MicroplasticDensityConcentrationNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MicroplasticDensityConcentrationNodeRepositoryPort {
    MicroplasticDensityConcentrationNode save(MicroplasticDensityConcentrationNode entity);
    Optional<MicroplasticDensityConcentrationNode> findById(String id, String tenantId);
}
