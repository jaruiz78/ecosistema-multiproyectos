package com.corp.subsurfacegeotwin.application;

import com.corp.subsurfacegeotwin.domain.SubSurfaceGeoTwinEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SubSurfaceGeoTwin.
 */
public class SubSurfaceGeoTwinUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SubSurfaceGeoTwinEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
