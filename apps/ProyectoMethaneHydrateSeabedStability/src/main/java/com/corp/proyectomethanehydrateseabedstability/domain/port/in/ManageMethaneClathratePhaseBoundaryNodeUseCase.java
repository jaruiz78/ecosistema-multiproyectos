package com.corp.proyectomethanehydrateseabedstability.domain.port.in;

import com.corp.proyectomethanehydrateseabedstability.domain.model.MethaneClathratePhaseBoundaryNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageMethaneClathratePhaseBoundaryNodeUseCase {
    MethaneClathratePhaseBoundaryNode createMethaneClathratePhaseBoundaryNode(String tenantId, String title, double value);
    Optional<MethaneClathratePhaseBoundaryNode> findMethaneClathratePhaseBoundaryNodeById(String id, String tenantId);
    MethaneClathratePhaseBoundaryNode processOptimization(String id, String tenantId);
}
