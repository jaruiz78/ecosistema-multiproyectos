package com.corp.biotecnologia.application;

import com.corp.biotecnologia.domain.BiotecnologiaEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para Biotecnologia.
 */
public class BiotecnologiaUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(BiotecnologiaEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio inyectada
            return input.cellCount() > 1000 ? input.phLevel() * 0.9 : input.phLevel();
        } finally {
            lock.unlock();
        }
    }
}
