package com.corp.parquesnacionalesnatura2000.application;

import com.corp.parquesnacionalesnatura2000.domain.ParquesNacionalesNatura2000Entity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para ParquesNacionalesNatura2000.
 */
public class ParquesNacionalesNatura2000UseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(ParquesNacionalesNatura2000Entity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
