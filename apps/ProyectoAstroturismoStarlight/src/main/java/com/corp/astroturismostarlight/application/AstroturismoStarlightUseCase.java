package com.corp.astroturismostarlight.application;

import com.corp.astroturismostarlight.domain.AstroturismoStarlightEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para AstroturismoStarlight.
 */
public class AstroturismoStarlightUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(AstroturismoStarlightEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
