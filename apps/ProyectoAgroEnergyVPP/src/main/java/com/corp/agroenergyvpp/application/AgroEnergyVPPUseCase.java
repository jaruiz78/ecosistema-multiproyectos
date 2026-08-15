package com.corp.agroenergyvpp.application;

import com.corp.agroenergyvpp.domain.AgroEnergyVPPEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para AgroEnergyVPP.
 */
public class AgroEnergyVPPUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(AgroEnergyVPPEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
