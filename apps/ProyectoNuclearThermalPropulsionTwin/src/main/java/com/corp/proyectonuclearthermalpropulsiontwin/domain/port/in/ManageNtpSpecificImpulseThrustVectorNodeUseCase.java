package com.corp.proyectonuclearthermalpropulsiontwin.domain.port.in;

import com.corp.proyectonuclearthermalpropulsiontwin.domain.model.NtpSpecificImpulseThrustVectorNode;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageNtpSpecificImpulseThrustVectorNodeUseCase {
    NtpSpecificImpulseThrustVectorNode createNtpSpecificImpulseThrustVectorNode(String tenantId, String title, double value);
    Optional<NtpSpecificImpulseThrustVectorNode> findNtpSpecificImpulseThrustVectorNodeById(String id, String tenantId);
    NtpSpecificImpulseThrustVectorNode processOptimization(String id, String tenantId);
}
