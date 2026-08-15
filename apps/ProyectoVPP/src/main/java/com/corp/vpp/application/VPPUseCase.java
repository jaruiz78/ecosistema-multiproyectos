package com.corp.vpp.application;

import com.corp.vpp.domain.VPPEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para VPP.
 */
public class VPPUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(VPPEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
