package com.corp.v2g.application;

import com.corp.v2g.domain.V2GEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para V2G.
 */
public class V2GUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(V2GEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
