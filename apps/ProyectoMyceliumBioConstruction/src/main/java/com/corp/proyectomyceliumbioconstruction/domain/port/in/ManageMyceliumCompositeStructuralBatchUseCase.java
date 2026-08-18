package com.corp.proyectomyceliumbioconstruction.domain.port.in;

import com.corp.proyectomyceliumbioconstruction.domain.model.MyceliumCompositeStructuralBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMyceliumCompositeStructuralBatchUseCase {
    MyceliumCompositeStructuralBatch createMyceliumCompositeStructuralBatch(String tenantId, String title, double value);
    Optional<MyceliumCompositeStructuralBatch> findMyceliumCompositeStructuralBatchById(String id, String tenantId);
    MyceliumCompositeStructuralBatch processOptimization(String id, String tenantId);
}
