package com.corp.syntheticbiologyfoundry.application;

import com.corp.syntheticbiologyfoundry.domain.SyntheticBiologyFoundryEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SyntheticBiologyFoundry.
 */
public class SyntheticBiologyFoundryUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SyntheticBiologyFoundryEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
