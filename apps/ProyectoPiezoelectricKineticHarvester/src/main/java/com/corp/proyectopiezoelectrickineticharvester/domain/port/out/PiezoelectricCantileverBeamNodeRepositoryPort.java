package com.corp.proyectopiezoelectrickineticharvester.domain.port.out;

import com.corp.proyectopiezoelectrickineticharvester.domain.model.PiezoelectricCantileverBeamNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface PiezoelectricCantileverBeamNodeRepositoryPort {
    PiezoelectricCantileverBeamNode save(PiezoelectricCantileverBeamNode entity);
    Optional<PiezoelectricCantileverBeamNode> findById(String id, String tenantId);
}
