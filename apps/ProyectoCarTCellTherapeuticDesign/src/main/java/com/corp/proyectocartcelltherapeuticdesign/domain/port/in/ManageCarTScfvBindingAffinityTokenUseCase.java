package com.corp.proyectocartcelltherapeuticdesign.domain.port.in;

import com.corp.proyectocartcelltherapeuticdesign.domain.model.CarTScfvBindingAffinityToken;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageCarTScfvBindingAffinityTokenUseCase {
    CarTScfvBindingAffinityToken createCarTScfvBindingAffinityToken(String tenantId, String title, double value);
    Optional<CarTScfvBindingAffinityToken> findCarTScfvBindingAffinityTokenById(String id, String tenantId);
    CarTScfvBindingAffinityToken processOptimization(String id, String tenantId);
}
