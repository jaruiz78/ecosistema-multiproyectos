package com.corp.clinicaltrialszk.application;

import com.corp.clinicaltrialszk.domain.ClinicalTrialsZKEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para ClinicalTrialsZK.
 */
public class ClinicalTrialsZKUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(ClinicalTrialsZKEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
