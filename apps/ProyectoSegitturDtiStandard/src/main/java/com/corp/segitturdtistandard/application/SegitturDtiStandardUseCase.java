package com.corp.segitturdtistandard.application;

import com.corp.segitturdtistandard.domain.SegitturDtiStandardEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SegitturDtiStandard.
 */
public class SegitturDtiStandardUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SegitturDtiStandardEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
