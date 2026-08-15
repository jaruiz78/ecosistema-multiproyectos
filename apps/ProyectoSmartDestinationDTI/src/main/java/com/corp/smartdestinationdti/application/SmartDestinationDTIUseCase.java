package com.corp.smartdestinationdti.application;

import com.corp.smartdestinationdti.domain.SmartDestinationDTIEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SmartDestinationDTI.
 */
public class SmartDestinationDTIUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SmartDestinationDTIEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
