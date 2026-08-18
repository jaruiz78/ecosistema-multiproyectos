package com.corp.proyectoredparadorestwin.domain.port.out;

import com.corp.proyectoredparadorestwin.domain.model.ParadorRoomNightStay;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface ParadorRoomNightStayRepositoryPort {
    ParadorRoomNightStay save(ParadorRoomNightStay entity);
    Optional<ParadorRoomNightStay> findById(String id, String tenantId);
}
