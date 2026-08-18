package com.corp.proyectoprecisionbiofermentationtwin.domain.port.in;

import com.corp.proyectoprecisionbiofermentationtwin.domain.model.FermentationBioreactorVesselNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageFermentationBioreactorVesselNodeUseCase {
    FermentationBioreactorVesselNode createFermentationBioreactorVesselNode(String tenantId, String title, double value);
    Optional<FermentationBioreactorVesselNode> findFermentationBioreactorVesselNodeById(String id, String tenantId);
    FermentationBioreactorVesselNode processOptimization(String id, String tenantId);
}
