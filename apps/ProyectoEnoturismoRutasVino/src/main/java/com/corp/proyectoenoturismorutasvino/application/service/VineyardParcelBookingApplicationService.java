package com.corp.proyectoenoturismorutasvino.application.service;

import com.corp.proyectoenoturismorutasvino.domain.model.VineyardParcelBooking;
import com.corp.proyectoenoturismorutasvino.domain.port.in.ManageVineyardParcelBookingUseCase;
import com.corp.proyectoenoturismorutasvino.domain.port.out.VineyardParcelBookingRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Aplicación para la orquestación de casos de uso de VineyardParcelBooking.
 */
@Service
public class VineyardParcelBookingApplicationService implements ManageVineyardParcelBookingUseCase {

    private final VineyardParcelBookingRepositoryPort repositoryPort;

    public VineyardParcelBookingApplicationService(VineyardParcelBookingRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public VineyardParcelBooking createVineyardParcelBooking(String tenantId, String title, double value) {
        VineyardParcelBooking entity = new VineyardParcelBooking(
            UUID.randomUUID().toString(),
            tenantId,
            title,
            value,
            "CREATED",
            Instant.now()
        );
        return repositoryPort.save(entity);
    }

    @Override
    public Optional<VineyardParcelBooking> findVineyardParcelBookingById(String id, String tenantId) {
        return repositoryPort.findById(id, tenantId);
    }

    @Override
    public VineyardParcelBooking processOptimization(String id, String tenantId) {
        VineyardParcelBooking existing = repositoryPort.findById(id, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Recurso no encontrado: " + id));
        VineyardParcelBooking optimized = existing.withStatus("OPTIMIZED");
        return repositoryPort.save(optimized);
    }
}
