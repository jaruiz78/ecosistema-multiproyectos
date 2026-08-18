package com.corp.proyectoenoturismorutasvino.domain.port.out;

import com.corp.proyectoenoturismorutasvino.domain.model.VineyardParcelBooking;
import java.util.Optional;

/**
 * Puerto de Salida para Persistencia y Streaming Analítico.
 */
public interface VineyardParcelBookingRepositoryPort {
    VineyardParcelBooking save(VineyardParcelBooking entity);
    Optional<VineyardParcelBooking> findById(String id, String tenantId);
}
