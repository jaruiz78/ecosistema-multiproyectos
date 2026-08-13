package com.proyecto.maritime.application;

import com.proyecto.maritime.domain.VesselBerthAssignment;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de optimización de atraque y despacho intermodal portuario.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
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
