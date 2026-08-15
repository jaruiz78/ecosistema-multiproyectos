package com.corp.coregeogridh3.application;

import com.corp.coregeogridh3.domain.Coregeogridh3Entity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Coregeogridh3.
 */
public class Coregeogridh3UseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(Coregeogridh3Entity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
