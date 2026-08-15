package com.corp.catastrofes.application;

import com.corp.catastrofes.domain.CatastrofesEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Catastrofes.
 */
public class CatastrofesUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CatastrofesEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
