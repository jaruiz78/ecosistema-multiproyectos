package com.corp.coreairagengine.application;

import com.corp.coreairagengine.domain.CoreairagengineEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Coreairagengine.
 */
public class CoreairagengineUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CoreairagengineEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
