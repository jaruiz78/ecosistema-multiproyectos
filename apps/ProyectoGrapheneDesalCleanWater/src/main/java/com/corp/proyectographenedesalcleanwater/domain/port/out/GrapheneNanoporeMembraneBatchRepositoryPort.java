package com.corp.proyectographenedesalcleanwater.domain.port.out;

import com.corp.proyectographenedesalcleanwater.domain.model.GrapheneNanoporeMembraneBatch;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface GrapheneNanoporeMembraneBatchRepositoryPort {
    GrapheneNanoporeMembraneBatch save(GrapheneNanoporeMembraneBatch entity);
    Optional<GrapheneNanoporeMembraneBatch> findById(String id, String tenantId);
}
