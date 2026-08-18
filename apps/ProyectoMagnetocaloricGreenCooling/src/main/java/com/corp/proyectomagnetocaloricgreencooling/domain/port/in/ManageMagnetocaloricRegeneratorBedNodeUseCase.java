package com.corp.proyectomagnetocaloricgreencooling.domain.port.in;

import com.corp.proyectomagnetocaloricgreencooling.domain.model.MagnetocaloricRegeneratorBedNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMagnetocaloricRegeneratorBedNodeUseCase {
    MagnetocaloricRegeneratorBedNode createMagnetocaloricRegeneratorBedNode(String tenantId, String title, double value);
    Optional<MagnetocaloricRegeneratorBedNode> findMagnetocaloricRegeneratorBedNodeById(String id, String tenantId);
    MagnetocaloricRegeneratorBedNode processOptimization(String id, String tenantId);
}
