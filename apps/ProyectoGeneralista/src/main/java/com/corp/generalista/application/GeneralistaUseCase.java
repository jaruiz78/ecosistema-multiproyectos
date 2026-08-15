package com.corp.generalista.application;

import com.corp.generalista.domain.GeneralistaEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Generalista.
 */
public class GeneralistaUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(GeneralistaEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
