package com.corp.miceconferencetwin.application;

import com.corp.miceconferencetwin.domain.MiceConferenceTwinEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para MiceConferenceTwin.
 */
public class MiceConferenceTwinUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(MiceConferenceTwinEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
