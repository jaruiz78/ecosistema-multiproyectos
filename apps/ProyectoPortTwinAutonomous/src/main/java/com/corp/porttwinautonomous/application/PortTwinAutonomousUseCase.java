package com.corp.porttwinautonomous.application;

import com.corp.porttwinautonomous.domain.PortTwinAutonomousEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para PortTwinAutonomous.
 */
public class PortTwinAutonomousUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(PortTwinAutonomousEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
