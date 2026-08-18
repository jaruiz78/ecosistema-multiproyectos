package com.corp.proyectogroundwater3daquifermodel.domain.port.out;

import com.corp.proyectogroundwater3daquifermodel.domain.model.AquiferHydraulicHeadPiezometerNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AquiferHydraulicHeadPiezometerNodeRepositoryPort {
    AquiferHydraulicHeadPiezometerNode save(AquiferHydraulicHeadPiezometerNode entity);
    Optional<AquiferHydraulicHeadPiezometerNode> findById(String id, String tenantId);
}
