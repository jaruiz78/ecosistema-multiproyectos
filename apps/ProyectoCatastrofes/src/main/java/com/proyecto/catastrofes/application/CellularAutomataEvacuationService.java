package com.proyecto.catastrofes.application;

import com.proyecto.catastrofes.domain.EvacuationZoneNode;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de evacuación mediante autómatas celulares sobre la malla espacial H3.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class CellularAutomataEvacuationService {

    private final ReentrantLock lock = new ReentrantLock();

    public EvacuationZoneNode stepEvacuation(EvacuationZoneNode zone, int transportCapacityPerStep) {
        lock.lock();
        try {
            if (zone.routeBlocked()) {
                return zone; // Sin avance si la vía está colapsada
            }
            int effectiveEvacuated = Math.min(zone.currentEvacueeCount(), transportCapacityPerStep);
            return zone.withEvacuationStep(effectiveEvacuated, false);
        } finally {
            lock.unlock();
        }
    }
}
