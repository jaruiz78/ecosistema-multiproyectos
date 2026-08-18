package com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.port.out;

import com.corp.proyectomicrobialelectrosynthesisbiofuel.domain.model.CathodeBiofilmElectronUptakeNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CathodeBiofilmElectronUptakeNodeRepositoryPort {
    CathodeBiofilmElectronUptakeNode save(CathodeBiofilmElectronUptakeNode entity);
    Optional<CathodeBiofilmElectronUptakeNode> findById(String id, String tenantId);
}
