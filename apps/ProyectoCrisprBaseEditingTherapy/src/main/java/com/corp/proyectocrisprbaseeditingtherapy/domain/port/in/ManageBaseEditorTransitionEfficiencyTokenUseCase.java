package com.corp.proyectocrisprbaseeditingtherapy.domain.port.in;

import com.corp.proyectocrisprbaseeditingtherapy.domain.model.BaseEditorTransitionEfficiencyToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageBaseEditorTransitionEfficiencyTokenUseCase {
    BaseEditorTransitionEfficiencyToken createBaseEditorTransitionEfficiencyToken(String tenantId, String title, double value);
    Optional<BaseEditorTransitionEfficiencyToken> findBaseEditorTransitionEfficiencyTokenById(String id, String tenantId);
    BaseEditorTransitionEfficiencyToken processOptimization(String id, String tenantId);
}
