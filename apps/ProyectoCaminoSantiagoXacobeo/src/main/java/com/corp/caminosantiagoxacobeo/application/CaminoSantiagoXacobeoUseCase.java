package com.corp.caminosantiagoxacobeo.application;

import com.corp.caminosantiagoxacobeo.domain.CaminoSantiagoXacobeoEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para CaminoSantiagoXacobeo.
 */
public class CaminoSantiagoXacobeoUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CaminoSantiagoXacobeoEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
