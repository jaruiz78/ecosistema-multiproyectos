package com.corp.circulartextiledpp.application;

import com.corp.circulartextiledpp.domain.CircularTextileDPPEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para CircularTextileDPP.
 */
public class CircularTextileDPPUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CircularTextileDPPEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
