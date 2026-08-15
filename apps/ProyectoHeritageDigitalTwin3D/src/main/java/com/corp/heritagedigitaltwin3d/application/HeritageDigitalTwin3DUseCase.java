package com.corp.heritagedigitaltwin3d.application;

import com.corp.heritagedigitaltwin3d.domain.HeritageDigitalTwin3DEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para HeritageDigitalTwin3D.
 */
public class HeritageDigitalTwin3DUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(HeritageDigitalTwin3DEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
