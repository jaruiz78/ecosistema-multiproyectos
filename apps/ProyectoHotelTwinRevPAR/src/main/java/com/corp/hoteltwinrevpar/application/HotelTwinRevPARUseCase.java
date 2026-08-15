package com.corp.hoteltwinrevpar.application;

import com.corp.hoteltwinrevpar.domain.HotelTwinRevPAREntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para HotelTwinRevPAR.
 */
public class HotelTwinRevPARUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(HotelTwinRevPAREntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
