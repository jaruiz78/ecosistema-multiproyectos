package com.corp.coresyncmesh.application;

import com.corp.coresyncmesh.domain.CoresyncmeshEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Coresyncmesh.
 */
public class CoresyncmeshUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CoresyncmeshEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
