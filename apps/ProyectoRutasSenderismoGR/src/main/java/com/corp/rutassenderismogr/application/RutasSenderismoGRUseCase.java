package com.corp.rutassenderismogr.application;

import com.corp.rutassenderismogr.domain.RutasSenderismoGREntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para RutasSenderismoGR.
 */
public class RutasSenderismoGRUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(RutasSenderismoGREntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
