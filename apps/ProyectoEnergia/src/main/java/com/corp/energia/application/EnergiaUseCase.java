package com.corp.energia.application;

import com.corp.energia.domain.EnergiaEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Energia.
 */
public class EnergiaUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(EnergiaEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.voltage() * input.current() * 0.95;
        } finally {
            lock.unlock();
        }
    }
}
