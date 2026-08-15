package com.corp.defensa.application;

import com.corp.defensa.domain.DefensaEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Defensa.
 */
public class DefensaUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(DefensaEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
