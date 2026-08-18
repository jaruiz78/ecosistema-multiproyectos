package com.corp.proyectographenedesalcleanwater.domain.port.in;

import com.corp.proyectographenedesalcleanwater.domain.model.GrapheneNanoporeMembraneBatch;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageGrapheneNanoporeMembraneBatchUseCase {
    GrapheneNanoporeMembraneBatch createGrapheneNanoporeMembraneBatch(String tenantId, String title, double value);
    Optional<GrapheneNanoporeMembraneBatch> findGrapheneNanoporeMembraneBatchById(String id, String tenantId);
    GrapheneNanoporeMembraneBatch processOptimization(String id, String tenantId);
}
