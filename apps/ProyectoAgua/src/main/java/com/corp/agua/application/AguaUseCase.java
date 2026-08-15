package com.corp.agua.application;

import com.corp.agua.domain.AguaEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Agua.
 */
public class AguaUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(AguaEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
