package com.corp.proyectocrisprbaseeditingtherapy.domain.port.out;

import com.corp.proyectocrisprbaseeditingtherapy.domain.model.BaseEditorTransitionEfficiencyToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface BaseEditorTransitionEfficiencyTokenRepositoryPort {
    BaseEditorTransitionEfficiencyToken save(BaseEditorTransitionEfficiencyToken entity);
    Optional<BaseEditorTransitionEfficiencyToken> findById(String id, String tenantId);
}
