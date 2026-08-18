package com.corp.proyectoprecisionbiofermentationtwin.domain.port.out;

import com.corp.proyectoprecisionbiofermentationtwin.domain.model.FermentationBioreactorVesselNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface FermentationBioreactorVesselNodeRepositoryPort {
    FermentationBioreactorVesselNode save(FermentationBioreactorVesselNode entity);
    Optional<FermentationBioreactorVesselNode> findById(String id, String tenantId);
}
