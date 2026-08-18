package com.corp.proyectofusionplasmatokamaktwin.domain.port.out;

import com.corp.proyectofusionplasmatokamaktwin.domain.model.PlasmaMhdMagneticFluxNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PlasmaMhdMagneticFluxNodeRepositoryPort {
    PlasmaMhdMagneticFluxNode save(PlasmaMhdMagneticFluxNode entity);
    Optional<PlasmaMhdMagneticFluxNode> findById(String id, String tenantId);
}
