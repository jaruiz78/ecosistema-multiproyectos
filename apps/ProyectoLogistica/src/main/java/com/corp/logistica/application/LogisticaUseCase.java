package com.corp.logistica.application;

import com.corp.logistica.domain.LogisticaEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Logistica.
 */
public class LogisticaUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(LogisticaEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
