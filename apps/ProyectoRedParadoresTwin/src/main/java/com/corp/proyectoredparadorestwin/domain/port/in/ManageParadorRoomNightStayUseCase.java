package com.corp.proyectoredparadorestwin.domain.port.in;

import com.corp.proyectoredparadorestwin.domain.model.ParadorRoomNightStay;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageParadorRoomNightStayUseCase {
    ParadorRoomNightStay createParadorRoomNightStay(String tenantId, String title, double value);
    Optional<ParadorRoomNightStay> findParadorRoomNightStayById(String id, String tenantId);
    ParadorRoomNightStay processOptimization(String id, String tenantId);
}
