package com.corp.fleetcoldchain.application;

import com.corp.fleetcoldchain.domain.FleetColdChainEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para FleetColdChain.
 */
public class FleetColdChainUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(FleetColdChainEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
