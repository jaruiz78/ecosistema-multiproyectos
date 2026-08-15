package com.corp.circular.application;

import com.corp.circular.domain.CircularEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Circular.
 */
public class CircularUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CircularEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
