package com.corp.proyectocoastalclifferosiondefense.domain.port.out;

import com.corp.proyectocoastalclifferosiondefense.domain.model.CliffRetreatErosionRateNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CliffRetreatErosionRateNodeRepositoryPort {
    CliffRetreatErosionRateNode save(CliffRetreatErosionRateNode entity);
    Optional<CliffRetreatErosionRateNode> findById(String id, String tenantId);
}
