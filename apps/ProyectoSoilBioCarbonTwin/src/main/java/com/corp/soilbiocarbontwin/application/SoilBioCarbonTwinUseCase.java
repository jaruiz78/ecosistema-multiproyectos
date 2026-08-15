package com.corp.soilbiocarbontwin.application;

import com.corp.soilbiocarbontwin.domain.SoilBioCarbonTwinEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SoilBioCarbonTwin.
 */
public class SoilBioCarbonTwinUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SoilBioCarbonTwinEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
