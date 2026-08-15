package com.corp.bioagritrace.application;

import com.corp.bioagritrace.domain.BioAgriTraceEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para BioAgriTrace.
 */
public class BioAgriTraceUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(BioAgriTraceEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
