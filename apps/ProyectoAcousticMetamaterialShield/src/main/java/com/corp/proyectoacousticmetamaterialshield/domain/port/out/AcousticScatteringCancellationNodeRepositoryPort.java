package com.corp.proyectoacousticmetamaterialshield.domain.port.out;

import com.corp.proyectoacousticmetamaterialshield.domain.model.AcousticScatteringCancellationNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface AcousticScatteringCancellationNodeRepositoryPort {
    AcousticScatteringCancellationNode save(AcousticScatteringCancellationNode entity);
    Optional<AcousticScatteringCancellationNode> findById(String id, String tenantId);
}
