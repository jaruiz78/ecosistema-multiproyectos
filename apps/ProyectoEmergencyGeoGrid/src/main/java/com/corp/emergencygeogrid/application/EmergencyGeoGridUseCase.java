package com.corp.emergencygeogrid.application;

import com.corp.emergencygeogrid.domain.EmergencyGeoGridEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para EmergencyGeoGrid.
 */
public class EmergencyGeoGridUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(EmergencyGeoGridEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
