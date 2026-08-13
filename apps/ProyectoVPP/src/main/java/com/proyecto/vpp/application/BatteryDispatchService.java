package com.proyecto.vpp.application;

import com.proyecto.vpp.domain.DistributedEnergyResource;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Servicio de aplicación para el despacho continuo de baterías VPP durante picos de demanda.
 * Loom Virtual Threads anti-pinning con ReentrantLock.
 */
public class BatteryDispatchService {

    private final ReentrantLock lock = new ReentrantLock();

    public DistributedEnergyResource dispatchPeakLoad(DistributedEnergyResource der, double requestedKw, double durationHours) {
        lock.lock();
        try {
            double effectiveKw = Math.min(requestedKw, der.maxDischargeKw());
            double energyDischargedKwh = effectiveKw * durationHours;
            return der.withDischarge(energyDischargedKwh);
        } finally {
            lock.unlock();
        }
    }
}
