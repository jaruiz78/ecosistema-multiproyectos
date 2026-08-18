package com.corp.proyectoatomicforcenanotopography.domain.port.out;

import com.corp.proyectoatomicforcenanotopography.domain.model.AfmCantileverDeflectionScanNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AfmCantileverDeflectionScanNodeRepositoryPort {
    AfmCantileverDeflectionScanNode save(AfmCantileverDeflectionScanNode entity);
    Optional<AfmCantileverDeflectionScanNode> findById(String id, String tenantId);
}
