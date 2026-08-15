package com.corp.airporttouristintermodal.application;

import com.corp.airporttouristintermodal.domain.AirportTouristIntermodalEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para AirportTouristIntermodal.
 */
public class AirportTouristIntermodalUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(AirportTouristIntermodalEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
