package com.corp.proyectoaavvectortherapeuticdesign.domain.port.in;

import com.corp.proyectoaavvectortherapeuticdesign.domain.model.AavCapsidTropismVectorToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageAavCapsidTropismVectorTokenUseCase {
    AavCapsidTropismVectorToken createAavCapsidTropismVectorToken(String tenantId, String title, double value);
    Optional<AavCapsidTropismVectorToken> findAavCapsidTropismVectorTokenById(String id, String tenantId);
    AavCapsidTropismVectorToken processOptimization(String id, String tenantId);
}
