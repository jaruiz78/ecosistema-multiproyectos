package com.corp.proyectoxenotransplantationimmunetwin.domain.port.in;

import com.corp.proyectoxenotransplantationimmunetwin.domain.model.XenoOrganCompatibilityScoreToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageXenoOrganCompatibilityScoreTokenUseCase {
    XenoOrganCompatibilityScoreToken createXenoOrganCompatibilityScoreToken(String tenantId, String title, double value);
    Optional<XenoOrganCompatibilityScoreToken> findXenoOrganCompatibilityScoreTokenById(String id, String tenantId);
    XenoOrganCompatibilityScoreToken processOptimization(String id, String tenantId);
}
