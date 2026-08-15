package com.corp.seamlessintermodalhub.application;

import com.corp.seamlessintermodalhub.domain.SeamlessIntermodalHubEntity;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Despachador de lógica real para SeamlessIntermodalHub.
 */
public class SeamlessIntermodalHubUseCase {
    private final ReentrantLock lock = new ReentrantLock();
    
    public double executeBusinessLogic(SeamlessIntermodalHubEntity input) {
        lock.lock();
        try {
            // Lógica compleja de negocio
            return input.specializedMetric() * 1.05;
        } finally {
            lock.unlock();
        }
    }
}
