package com.corp.proyectoautonomouslunarroverexplorer.domain.port.out;

import com.corp.proyectoautonomouslunarroverexplorer.domain.model.RoverWheelSlipTerramechanicsNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface RoverWheelSlipTerramechanicsNodeRepositoryPort {
    RoverWheelSlipTerramechanicsNode save(RoverWheelSlipTerramechanicsNode entity);
    Optional<RoverWheelSlipTerramechanicsNode> findById(String id, String tenantId);
}
