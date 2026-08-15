package com.corp.droneairspaceuspace.application;

import com.corp.droneairspaceuspace.domain.DroneAirspaceUSpaceEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para DroneAirspaceUSpace.
 */
public class DroneAirspaceUSpaceUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(DroneAirspaceUSpaceEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.altimeter() > 1000 ? input.altimeter() * 0.9 : input.altimeter();
        } finally {
            lock.unlock();
        }
    }
}
