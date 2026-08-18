package com.corp.proyectomethanehydrateseabedstability.domain.port.out;

import com.corp.proyectomethanehydrateseabedstability.domain.model.MethaneClathratePhaseBoundaryNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface MethaneClathratePhaseBoundaryNodeRepositoryPort {
    MethaneClathratePhaseBoundaryNode save(MethaneClathratePhaseBoundaryNode entity);
    Optional<MethaneClathratePhaseBoundaryNode> findById(String id, String tenantId);
}
