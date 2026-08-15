package com.corp.globalcruisemrv.application;

import com.corp.globalcruisemrv.domain.GlobalCruiseMRVEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para GlobalCruiseMRV.
 */
public class GlobalCruiseMRVUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(GlobalCruiseMRVEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
