package com.corp.criticalmineralsmrv.application;

import com.corp.criticalmineralsmrv.domain.CriticalMineralsMRVEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para CriticalMineralsMRV.
 */
public class CriticalMineralsMRVUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CriticalMineralsMRVEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
