package com.corp.greenhydrogendesal.application;

import com.corp.greenhydrogendesal.domain.GreenHydrogenDesalEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para GreenHydrogenDesal.
 */
public class GreenHydrogenDesalUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(GreenHydrogenDesalEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
