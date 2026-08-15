package com.corp.fiestasinteresturistico.application;

import com.corp.fiestasinteresturistico.domain.FiestasInteresTuristicoEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para FiestasInteresTuristico.
 */
public class FiestasInteresTuristicoUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(FiestasInteresTuristicoEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
