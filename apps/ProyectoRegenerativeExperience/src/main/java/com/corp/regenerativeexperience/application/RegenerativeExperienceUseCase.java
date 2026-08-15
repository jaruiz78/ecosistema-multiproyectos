package com.corp.regenerativeexperience.application;

import com.corp.regenerativeexperience.domain.RegenerativeExperienceEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para RegenerativeExperience.
 */
public class RegenerativeExperienceUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(RegenerativeExperienceEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
