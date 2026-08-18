package com.corp.proyectocircularbiomassbiorefinery.domain.port.in;

import com.corp.proyectocircularbiomassbiorefinery.domain.model.BiomassFeedstockBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBiomassFeedstockBatchUseCase {
    BiomassFeedstockBatch createBiomassFeedstockBatch(String tenantId, String title, double value);
    Optional<BiomassFeedstockBatch> findBiomassFeedstockBatchById(String id, String tenantId);
    BiomassFeedstockBatch processOptimization(String id, String tenantId);
}
