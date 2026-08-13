package com.proyecto.circular.application;

import com.proyecto.circular.domain.BioWasteBatch;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de trazabilidad de Economía Circular y auditoría LCA.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
 */
public class WasteTraceabilityService {

    private final ReentrantLock lock = new ReentrantLock();

    public BioWasteBatch certifyBatchLca(BioWasteBatch batch, double minRecycledRatioThreshold) {
        lock.lock();
        try {
            boolean compliant = batch.recycledRatioPercent() >= minRecycledRatioThreshold;
            return batch.withCertification(compliant);
        } finally {
            lock.unlock();
        }
    }
}
