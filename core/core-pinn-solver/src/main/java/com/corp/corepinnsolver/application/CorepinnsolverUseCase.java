package com.corp.corepinnsolver.application;

import com.corp.corepinnsolver.domain.CorepinnsolverEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Corepinnsolver.
 */
public class CorepinnsolverUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(CorepinnsolverEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
