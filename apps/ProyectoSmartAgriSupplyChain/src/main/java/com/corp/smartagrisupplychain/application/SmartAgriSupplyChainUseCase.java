package com.corp.smartagrisupplychain.application;

import com.corp.smartagrisupplychain.domain.SmartAgriSupplyChainEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SmartAgriSupplyChain.
 */
public class SmartAgriSupplyChainUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SmartAgriSupplyChainEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
