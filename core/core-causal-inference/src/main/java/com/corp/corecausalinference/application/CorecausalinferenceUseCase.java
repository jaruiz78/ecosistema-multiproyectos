package com.corp.corecausalinference.application;

import com.corp.corecausalinference.domain.CorecausalinferenceEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Corecausalinference.
 */
public class CorecausalinferenceUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CorecausalinferenceEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
