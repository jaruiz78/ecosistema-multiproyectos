package com.corp.proyectoacousticmetamaterialshield.domain.port.in;

import com.corp.proyectoacousticmetamaterialshield.domain.model.AcousticScatteringCancellationNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAcousticScatteringCancellationNodeUseCase {
    AcousticScatteringCancellationNode createAcousticScatteringCancellationNode(String tenantId, String title, double value);
    Optional<AcousticScatteringCancellationNode> findAcousticScatteringCancellationNodeById(String id, String tenantId);
    AcousticScatteringCancellationNode processOptimization(String id, String tenantId);
}
