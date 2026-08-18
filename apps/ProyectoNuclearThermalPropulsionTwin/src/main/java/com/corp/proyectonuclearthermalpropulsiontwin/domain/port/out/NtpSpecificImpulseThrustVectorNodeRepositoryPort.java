package com.corp.proyectonuclearthermalpropulsiontwin.domain.port.out;

import com.corp.proyectonuclearthermalpropulsiontwin.domain.model.NtpSpecificImpulseThrustVectorNode;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface NtpSpecificImpulseThrustVectorNodeRepositoryPort {
    NtpSpecificImpulseThrustVectorNode save(NtpSpecificImpulseThrustVectorNode entity);
    Optional<NtpSpecificImpulseThrustVectorNode> findById(String id, String tenantId);
}
