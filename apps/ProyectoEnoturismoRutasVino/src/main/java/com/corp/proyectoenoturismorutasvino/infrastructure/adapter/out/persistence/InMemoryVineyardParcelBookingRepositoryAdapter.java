package com.corp.proyectoenoturismorutasvino.infrastructure.adapter.out.persistence;

import com.corp.proyectoenoturismorutasvino.domain.model.VineyardParcelBooking;
import com.corp.proyectoenoturismorutasvino.domain.port.out.VineyardParcelBookingRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryVineyardParcelBookingRepositoryAdapter implements VineyardParcelBookingRepositoryPort {

    private final ConcurrentMap<String, VineyardParcelBooking> storage = new ConcurrentHashMap<>();

    @Override
    public VineyardParcelBooking save(VineyardParcelBooking entity) {
        storage.put(entity.id(), entity);
        return entity;
    }

    @Override
    public Optional<VineyardParcelBooking> findById(String id, String tenantId) {
        VineyardParcelBooking entity = storage.get(id);
        if (entity != null && entity.tenantId().equals(tenantId)) {
            return Optional.of(entity);
        }
        return Optional.empty();
    }
}
