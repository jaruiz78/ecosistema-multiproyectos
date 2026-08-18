package com.corp.proyectofusionplasmatokamaktwin.domain.port.in;

import com.corp.proyectofusionplasmatokamaktwin.domain.model.PlasmaMhdMagneticFluxNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManagePlasmaMhdMagneticFluxNodeUseCase {
    PlasmaMhdMagneticFluxNode createPlasmaMhdMagneticFluxNode(String tenantId, String title, double value);
    Optional<PlasmaMhdMagneticFluxNode> findPlasmaMhdMagneticFluxNodeById(String id, String tenantId);
    PlasmaMhdMagneticFluxNode processOptimization(String id, String tenantId);
}
