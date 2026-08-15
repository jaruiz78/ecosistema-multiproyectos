package com.corp.corekalmantwin.application;

import com.corp.corekalmantwin.domain.CorekalmantwinEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Corekalmantwin.
 */
public class CorekalmantwinUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CorekalmantwinEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
