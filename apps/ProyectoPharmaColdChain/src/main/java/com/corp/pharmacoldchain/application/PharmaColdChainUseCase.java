package com.corp.pharmacoldchain.application;

import com.corp.pharmacoldchain.domain.PharmaColdChainEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para PharmaColdChain.
 */
public class PharmaColdChainUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(PharmaColdChainEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
