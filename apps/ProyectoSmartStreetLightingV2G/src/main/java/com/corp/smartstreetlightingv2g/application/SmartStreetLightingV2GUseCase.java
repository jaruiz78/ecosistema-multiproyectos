package com.corp.smartstreetlightingv2g.application;

import com.corp.smartstreetlightingv2g.domain.SmartStreetLightingV2GEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SmartStreetLightingV2G.
 */
public class SmartStreetLightingV2GUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SmartStreetLightingV2GEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
