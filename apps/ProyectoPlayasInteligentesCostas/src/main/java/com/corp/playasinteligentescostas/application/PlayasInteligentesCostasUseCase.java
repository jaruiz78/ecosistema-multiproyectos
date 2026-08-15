package com.corp.playasinteligentescostas.application;

import com.corp.playasinteligentescostas.domain.PlayasInteligentesCostasEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para PlayasInteligentesCostas.
 */
public class PlayasInteligentesCostasUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(PlayasInteligentesCostasEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
