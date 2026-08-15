package com.proyecto.maritime.application;

import com.proyecto.maritime.domain.VesselBerthAssignment;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de optimización de atraque y despacho intermodal portuario.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
  *
 * @see <a href="file:///home/jaruiz/Desarrollo/docs/adr/adr-004-firestore-rls-bigquery-finops.md">ADR de Referencia</a>
 * @see <a href="file:///home/jaruiz/Desarrollo/apps/VERTICALS_ARCHITECTURE_SPEC.md">Documentación y Módulo Formativo</a>
 * @reference Evans (2003) Domain-Driven Design (Tackling Complexity in Software)
 
 */
public class PortBerthOptimizerService {

    private final ReentrantLock lock = new ReentrantLock();

    public VesselBerthAssignment optimizeBerthSlot(VesselBerthAssignment assignment, String targetBerthId, double craneEfficiencyTeuPerHour) {
        lock.lock();
        try {
            long turnaroundMinutes = Math.round((assignment.containerTeuCount() / craneEfficiencyTeuPerHour) * 60.0);
            return assignment.withAllocation(targetBerthId, turnaroundMinutes);
        } finally {
            lock.unlock();
        }
    }
}
