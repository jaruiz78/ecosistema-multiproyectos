package com.corp.proyectoautonomouslunarroverexplorer.domain.port.in;

import com.corp.proyectoautonomouslunarroverexplorer.domain.model.RoverWheelSlipTerramechanicsNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageRoverWheelSlipTerramechanicsNodeUseCase {
    RoverWheelSlipTerramechanicsNode createRoverWheelSlipTerramechanicsNode(String tenantId, String title, double value);
    Optional<RoverWheelSlipTerramechanicsNode> findRoverWheelSlipTerramechanicsNodeById(String id, String tenantId);
    RoverWheelSlipTerramechanicsNode processOptimization(String id, String tenantId);
}
