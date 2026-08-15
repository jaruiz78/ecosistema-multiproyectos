package com.corp.smartwaterdesal.application;

import com.corp.smartwaterdesal.domain.SmartWaterDesalEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SmartWaterDesal.
 */
public class SmartWaterDesalUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SmartWaterDesalEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
