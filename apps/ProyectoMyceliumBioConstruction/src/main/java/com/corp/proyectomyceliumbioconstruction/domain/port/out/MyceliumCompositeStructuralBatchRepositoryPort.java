package com.corp.proyectomyceliumbioconstruction.domain.port.out;

import com.corp.proyectomyceliumbioconstruction.domain.model.MyceliumCompositeStructuralBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MyceliumCompositeStructuralBatchRepositoryPort {
    MyceliumCompositeStructuralBatch save(MyceliumCompositeStructuralBatch entity);
    Optional<MyceliumCompositeStructuralBatch> findById(String id, String tenantId);
}
