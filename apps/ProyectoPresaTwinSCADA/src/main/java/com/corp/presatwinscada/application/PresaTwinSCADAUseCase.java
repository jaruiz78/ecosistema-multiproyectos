package com.corp.presatwinscada.application;

import com.corp.presatwinscada.domain.PresaTwinSCADAEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para PresaTwinSCADA.
 */
public class PresaTwinSCADAUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(PresaTwinSCADAEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
