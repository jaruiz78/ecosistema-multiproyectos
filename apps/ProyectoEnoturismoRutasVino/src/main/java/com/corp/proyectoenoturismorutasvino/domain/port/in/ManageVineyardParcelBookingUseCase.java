package com.corp.proyectoenoturismorutasvino.domain.port.in;

import com.corp.proyectoenoturismorutasvino.domain.model.VineyardParcelBooking;
import java.util.Optional;

/**
 * Puerto de Entrada (Caso de Uso de Negocio).
 */
public interface ManageVineyardParcelBookingUseCase {
    VineyardParcelBooking createVineyardParcelBooking(String tenantId, String title, double value);
    Optional<VineyardParcelBooking> findVineyardParcelBookingById(String id, String tenantId);
    VineyardParcelBooking processOptimization(String id, String tenantId);
}
