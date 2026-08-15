package com.corp.maritime.application;

import com.corp.maritime.domain.MaritimeEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Maritime.
 */
public class MaritimeUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(MaritimeEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
