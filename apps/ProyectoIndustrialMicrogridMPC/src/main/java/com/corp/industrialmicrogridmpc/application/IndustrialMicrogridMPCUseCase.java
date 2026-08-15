package com.corp.industrialmicrogridmpc.application;

import com.corp.industrialmicrogridmpc.domain.IndustrialMicrogridMPCEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para IndustrialMicrogridMPC.
 */
public class IndustrialMicrogridMPCUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(IndustrialMicrogridMPCEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
