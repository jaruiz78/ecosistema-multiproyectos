package com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.in;

import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.model.CathodeBiofilmElectronUptakeNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCathodeBiofilmElectronUptakeNodeUseCase {
    CathodeBiofilmElectronUptakeNode createCathodeBiofilmElectronUptakeNode(String tenantId, String title, double value);
    Optional<CathodeBiofilmElectronUptakeNode> findCathodeBiofilmElectronUptakeNodeById(String id, String tenantId);
    CathodeBiofilmElectronUptakeNode processOptimization(String id, String tenantId);
}
