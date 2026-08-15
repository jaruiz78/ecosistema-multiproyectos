package com.corp.diputacionturismorural.application;

import com.corp.diputacionturismorural.domain.DiputacionTurismoRuralEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para DiputacionTurismoRural.
 */
public class DiputacionTurismoRuralUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(DiputacionTurismoRuralEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
