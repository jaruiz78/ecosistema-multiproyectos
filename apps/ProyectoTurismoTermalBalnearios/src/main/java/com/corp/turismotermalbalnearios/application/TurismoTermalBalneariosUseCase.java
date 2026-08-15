package com.corp.turismotermalbalnearios.application;

import com.corp.turismotermalbalnearios.domain.TurismoTermalBalneariosEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para TurismoTermalBalnearios.
 */
public class TurismoTermalBalneariosUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(TurismoTermalBalneariosEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
