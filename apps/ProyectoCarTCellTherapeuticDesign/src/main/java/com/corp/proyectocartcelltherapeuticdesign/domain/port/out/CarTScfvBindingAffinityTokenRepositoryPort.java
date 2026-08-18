package com.corp.proyectocartcelltherapeuticdesign.domain.port.out;

import com.corp.proyectocartcelltherapeuticdesign.domain.model.CarTScfvBindingAffinityToken;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface CarTScfvBindingAffinityTokenRepositoryPort {
    CarTScfvBindingAffinityToken save(CarTScfvBindingAffinityToken entity);
    Optional<CarTScfvBindingAffinityToken> findById(String id, String tenantId);
}
