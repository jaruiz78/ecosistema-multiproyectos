package com.corp.proyectoxenotransplantationimmunetwin.domain.port.out;

import com.corp.proyectoxenotransplantationimmunetwin.domain.model.XenoOrganCompatibilityScoreToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface XenoOrganCompatibilityScoreTokenRepositoryPort {
    XenoOrganCompatibilityScoreToken save(XenoOrganCompatibilityScoreToken entity);
    Optional<XenoOrganCompatibilityScoreToken> findById(String id, String tenantId);
}
