package com.proyecto.circular.application;

import com.proyecto.circular.domain.BioWasteBatch;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de trazabilidad de Economía Circular y auditoría LCA.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
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
