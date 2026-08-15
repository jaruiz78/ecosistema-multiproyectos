package com.corp.airlineinterlinebaggage.application;

import com.corp.airlineinterlinebaggage.domain.AirlineInterlineBaggageEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para AirlineInterlineBaggage.
 */
public class AirlineInterlineBaggageUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(AirlineInterlineBaggageEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
