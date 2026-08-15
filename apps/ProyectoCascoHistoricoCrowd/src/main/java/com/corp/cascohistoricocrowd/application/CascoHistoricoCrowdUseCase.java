package com.corp.cascohistoricocrowd.application;

import com.corp.cascohistoricocrowd.domain.CascoHistoricoCrowdEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para CascoHistoricoCrowd.
 */
public class CascoHistoricoCrowdUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CascoHistoricoCrowdEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
