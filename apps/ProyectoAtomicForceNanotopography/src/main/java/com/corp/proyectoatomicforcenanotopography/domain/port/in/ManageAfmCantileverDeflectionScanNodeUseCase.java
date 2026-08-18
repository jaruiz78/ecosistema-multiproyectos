package com.corp.proyectoatomicforcenanotopography.domain.port.in;

import com.corp.proyectoatomicforcenanotopography.domain.model.AfmCantileverDeflectionScanNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAfmCantileverDeflectionScanNodeUseCase {
    AfmCantileverDeflectionScanNode createAfmCantileverDeflectionScanNode(String tenantId, String title, double value);
    Optional<AfmCantileverDeflectionScanNode> findAfmCantileverDeflectionScanNodeById(String id, String tenantId);
    AfmCantileverDeflectionScanNode processOptimization(String id, String tenantId);
}
