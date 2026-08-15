package com.corp.ecotourismpassport.application;

import com.corp.ecotourismpassport.domain.EcoTourismPassportEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para EcoTourismPassport.
 */
public class EcoTourismPassportUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(EcoTourismPassportEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
