package com.corp.dualairdefense.application;

import com.corp.dualairdefense.domain.DualAirDefenseEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para DualAirDefense.
 */
public class DualAirDefenseUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(DualAirDefenseEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
