package com.corp.proyectocircularbiomassbiorefinery.domain.port.out;

import com.corp.proyectocircularbiomassbiorefinery.domain.model.BiomassFeedstockBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BiomassFeedstockBatchRepositoryPort {
    BiomassFeedstockBatch save(BiomassFeedstockBatch entity);
    Optional<BiomassFeedstockBatch> findById(String id, String tenantId);
}
